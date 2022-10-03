#include <bnb/glsl.frag>

// #define TEETH_WHITENING
// #define teethWhiteningCoeff 1.0
#define SOFT_SKIN
#define skinSoftIntensity 0.7

#define EYES_WHITENING
#define eyesWhiteningCoeff 0.1
#define NORMAL_LAYER
#define SHARPEN_TEETH
#define teethSharpenIntensity 0.2
#define SHARPEN_EYES
#define eyesSharpenIntensity 0.2
#define PSI 0.1
#define SOFT_LIGHT_LAYER


BNB_IN(0) vec2 var_uv;
BNB_IN(1) vec2 var_bg_uv;
BNB_IN(2) mat4 sp;


BNB_DECLARE_SAMPLER_2D(0, 1, selection_tex);

BNB_DECLARE_SAMPLER_2D(2, 3, lookupTexEyes);

BNB_DECLARE_SAMPLER_2D(8, 9, glfx_BACKGROUND);

#ifdef NORMAL_LAYER

BNB_DECLARE_SAMPLER_2D(4, 5, tex_normalMakeup);
#endif

#ifdef SOFT_LIGHT_LAYER

BNB_DECLARE_SAMPLER_2D(6, 7, tex_softLight);
#endif


#define YUV2RGB_RED_CrV 1.402
#define YUV2RGB_GREEN_CbU 0.3441
#define YUV2RGB_GREEN_CrV 0.7141
#define YUV2RGB_BLUE_CbU 1.772

vec4 rgba2yuva (vec4 rgba)
{
	vec4 yuva = vec4(0.);

	yuva.x = rgba.r * 0.299 + rgba.g * 0.587 + rgba.b * 0.114;
	yuva.y = rgba.r * -0.169 + rgba.g * -0.331 + rgba.b * 0.5 + 0.5;
	yuva.z = rgba.r * 0.5 + rgba.g * -0.419 + rgba.b * -0.081 + 0.5;
	yuva.w = rgba.a;

	return yuva;
}


vec3 blendMultiply(vec3 base, vec3 blend) {
	return base*blend;
}

vec3 blendMultiply(vec3 base, vec3 blend, float opacity) {
	return (blendMultiply(base, blend) * opacity + base * (1.0 - opacity));
}

float blendOverlay(float base, float blend) {
	return base<0.5?(2.0*base*blend):(1.0-2.0*(1.0-base)*(1.0-blend));
}

vec3 blendOverlay(vec3 base, vec3 blend) {
	return vec3(blendOverlay(base.r,blend.r),blendOverlay(base.g,blend.g),blendOverlay(base.b,blend.b));
}

vec3 blendOverlay(vec3 base, vec3 blend, float opacity) {
	return (blendOverlay(base, blend) * opacity + base * (1.0 - opacity));
}

vec4 textureLookup(vec4 originalColor, BNB_DECLARE_SAMPLER_2D_ARGUMENT(lookupTexture))
{
    const float epsilon = 0.000001;
    const float lutSize = 512.0;

    float blueValue = (originalColor.b * 255.0) / 4.0;

    vec2 mulB = clamp(floor(blueValue) + vec2(0.0, 1.0), 0.0, 63.0);
    vec2 row = floor(mulB / 8.0 + epsilon);
    vec4 row_col = vec4(row, mulB - row * 8.0);
    vec4 lookup = originalColor.ggrr * (63.0 / lutSize) + row_col * (64.0 / lutSize) + (0.5 / lutSize);

    float factor = blueValue - mulB.x;

    vec3 sampled1 = BNB_TEXTURE_2D_LOD(BNB_SAMPLER_2D(lookupTexture), lookup.zx, 0.).rgb;
    vec3 sampled2 = BNB_TEXTURE_2D_LOD(BNB_SAMPLER_2D(lookupTexture), lookup.wy, 0.).rgb;

    vec3 res = mix(sampled1, sampled2, factor);
    return vec4(res, originalColor.a);
}

vec4 whitening(vec4 originalColor, float factor, BNB_DECLARE_SAMPLER_2D_ARGUMENT(lookup)) {
    vec4 color = textureLookup(originalColor, BNB_PASS_SAMPLER_ARGUMENT(lookup));
    return mix(originalColor, color, factor);
}

vec4 sharpen(vec4 originalColor, float factor) {
    vec4 total = 5.0 * originalColor - BNB_TEXTURE_2D(BNB_SAMPLER_2D(glfx_BACKGROUND), sp[0].zw) - BNB_TEXTURE_2D(BNB_SAMPLER_2D(glfx_BACKGROUND), sp[1].zw) - BNB_TEXTURE_2D(BNB_SAMPLER_2D(glfx_BACKGROUND), sp[2].zw) - BNB_TEXTURE_2D(BNB_SAMPLER_2D(glfx_BACKGROUND), sp[3].zw);
    vec4 result = mix(originalColor, total, factor);
    return clamp(result, 0.0, 1.0);;
}

vec4 getLuminance4(mat4 color) {
    const vec4 rgb2y = vec4(0.299, 0.587, 0.114, 0.0);
    return rgb2y * color;
}

float getLuminance(vec4 color) {
    const vec4 rgb2y = vec4(0.299, 0.587, 0.114, 0.0);
    return dot(color, rgb2y);
}

vec4 getWeight(float intensity, vec4 nextIntensity) {
    vec4 lglg = log(nextIntensity / intensity) * log(nextIntensity / intensity);
    return exp(lglg / (-2.0 *  PSI  *  PSI ));
}

vec4 softSkin(vec4 originalColor, float factor) {
    vec4 screenColor = originalColor;
    float intensity = getLuminance(screenColor);
    float summ = 1.0;
    
    mat4 nextColor;
    nextColor[0] = BNB_TEXTURE_2D(BNB_SAMPLER_2D(glfx_BACKGROUND), sp[0].xy);
    nextColor[1] = BNB_TEXTURE_2D(BNB_SAMPLER_2D(glfx_BACKGROUND), sp[1].xy);
    nextColor[2] = BNB_TEXTURE_2D(BNB_SAMPLER_2D(glfx_BACKGROUND), sp[2].xy);
    nextColor[3] = BNB_TEXTURE_2D(BNB_SAMPLER_2D(glfx_BACKGROUND), sp[3].xy);
    vec4 nextIntensity = getLuminance4(nextColor);
    vec4 curr = 0.367 * getWeight(intensity, nextIntensity);
    summ += dot(curr, vec4(1.0));
    screenColor += nextColor * curr;
    screenColor = screenColor / summ;
    
    screenColor = mix(originalColor, screenColor, factor);
    return screenColor;
}

float blendSoftLight(float a, float b) {
    return (a+2.*b*(1.-a))*a;
}

vec3 blendSoftLight(vec3 base, vec3 blend) {
    return vec3(blendSoftLight(base.r,blend.r),blendSoftLight(base.g,blend.g),blendSoftLight(base.b,blend.b));
}

vec3 blendSoftLight(vec3 base, vec3 blend, float opacity) {
    return (blendSoftLight(base, blend) * opacity + base * (1.0 - opacity));
}

void main()
{
    vec4 maskColor = BNB_TEXTURE_2D(BNB_SAMPLER_2D(selection_tex), var_uv);
    vec4 res = BNB_TEXTURE_2D(BNB_SAMPLER_2D(glfx_BACKGROUND), var_bg_uv );

	vec4 js_yuva = rgba2yuva(face_color);
	vec2 maskColor2 = js_yuva.yz;
	float beta = js_yuva.x;

	float y = dot(res.xyz, vec3(0.2989,0.5866,0.1144));

	vec2 uv_src = vec2(
		dot(res.xyz, vec3(-0.1688,-0.3312,0.5)) + 0.5,
		dot(res.xyz, vec3(0.5,-0.4183,-0.0816)) + 0.5);

	float alpha = maskColor.r;

	vec2 uv45 = (1.0 - alpha) * uv_src + alpha * ((1.0 - beta) * maskColor2 + beta * uv_src);

	float u = uv45.x - 0.5;
	float v = uv45.y - 0.5;

	float r = y + YUV2RGB_RED_CrV * v;
	float g = y - YUV2RGB_GREEN_CbU * u - YUV2RGB_GREEN_CrV * v;
	float b = y + YUV2RGB_BLUE_CbU * u;

	vec4 color = vec4(r, g, b, 1.0);

    res = color;
/*
    vec4 make_multi = texture( tex_make_multi, var_uv );
    vec4 make_overlay = texture( tex_make_overlay, var_uv );
    vec4 shadow_multi = texture( tex_shadow_multi, var_uv );
    vec4 shadow_overlay = texture( tex_shadow_overlay, var_uv );
    vec4 shadow2_overlay = texture( tex_shadow2_overlay, var_uv );
*/    
#ifdef SOFT_SKIN
    res = softSkin(res, maskColor.r * skinSoftIntensity);
#endif
    
#ifdef SKIN_TEXTURING
    vec4 skinTexture = texture(skin_tex, var_uv);
    vec4 diff = abs(skinTexture - res);
    res = mix(res, diff, skinTexturingIntensity);
#endif
    
#ifdef SHARPEN_TEETH
    res = sharpen(res, maskColor.g * teethSharpenIntensity);
#endif
    
#if defined(TEETH_WHITENING)
    res = whitening(res, maskColor.g * teethWhiteningCoeff, lookupTexTeeth);
#endif
    
    
#ifdef SHARPEN_EYES
    res = sharpen(res, maskColor.b * eyesSharpenIntensity);
#endif
    
#if defined(EYES_WHITENING)
    res = whitening(res, maskColor.b * eyesWhiteningCoeff, BNB_PASS_SAMPLER_ARGUMENT(lookupTexEyes));
#endif
/*
    res.rgb = blendMultiply(res.rgb,make_multi.rgb,make_multi.a);
    res.rgb = blendOverlay(res.rgb,make_overlay.rgb,make_overlay.a);
    res.rgb = blendMultiply(res.rgb,shadow_multi.rgb,shadow_multi.a);
    res.rgb = blendOverlay(res.rgb,shadow_overlay.rgb,shadow_overlay.a);
    res.rgb = blendOverlay(res.rgb,shadow2_overlay.rgb,shadow2_overlay.a);
*/

vec2 uvh = vec2(abs(2.0 * (var_uv.x - 0.5)),var_uv.y);

#ifdef SOFT_LIGHT_LAYER

    res.xyz = blendSoftLight( res.xyz, BNB_TEXTURE_2D(BNB_SAMPLER_2D(tex_softLight), uvh ).xyz, 0.3 );
#endif

#ifdef NORMAL_LAYER
    vec4 makeup2 = 0.7 * BNB_TEXTURE_2D(BNB_SAMPLER_2D(tex_normalMakeup), uvh );
    res.xyz = mix( res.xyz, 1.0 * makeup2.xyz, 1.0 * makeup2.w );

#endif
    bnb_FragColor = res;
}
