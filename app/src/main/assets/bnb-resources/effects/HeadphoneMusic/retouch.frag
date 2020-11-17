#version 300 es

#define PSI 0.1

#define SOFT_SKIN
#define skinTexturingIntensity 0.12
#define skinSoftIntensity 0.7

#define TEETH_WHITENING
#define teethSharpenIntensity 0.2
#define teethWhiteningCoeff 1.0

#define SHARPEN_EYES
// #define EYES_WHITENING
#define eyesSharpenIntensity 0.3
#define eyesWhiteningCoeff 0.5

#define EYES_HIGHLIGHT

precision highp float;

layout( location = 0 ) out vec4 F;
in vec2 var_uv, var_bg_uv;
in mat4 sp;



uniform sampler2D selection_tex, lookupTexEyes, lookupTexTeeth, glfx_BACKGROUND;

#if defined(EYES_HIGHLIGHT)
uniform sampler2D tex_highlight;
#endif

vec4 textureLookup(vec4 originalColor, sampler2D lookupTexture)
{
    const float epsilon = 0.000001;
    const float lutSize = 512.0;

    float blueValue = (originalColor.b * 255.0) / 4.0;

    vec2 mulB = clamp(floor(blueValue) + vec2(0.0, 1.0), 0.0, 63.0);
    vec2 row = floor(mulB / 8.0 + epsilon);
    vec4 row_col = vec4(row, mulB - row * 8.0);
    vec4 lookup = originalColor.ggrr * (63.0 / lutSize) + row_col * (64.0 / lutSize) + (0.5 / lutSize);

    float factor = blueValue - mulB.x;

    vec3 sampled1 = textureLod(lookupTexture, lookup.zx, 0.).rgb;
    vec3 sampled2 = textureLod(lookupTexture, lookup.wy, 0.).rgb;

    vec3 res = mix(sampled1, sampled2, factor);
    return vec4(res, originalColor.a);
}

vec4 whitening(vec4 originalColor, float factor, sampler2D lookup) {
    vec4 color = textureLookup(originalColor, lookup);
    return mix(originalColor, color, factor);
}

vec4 sharpen(vec4 originalColor, float factor) {
    vec4 total = 5.0 * originalColor - texture(glfx_BACKGROUND, sp[0].zw) - texture(glfx_BACKGROUND, sp[1].zw) - texture(glfx_BACKGROUND, sp[2].zw) - texture(glfx_BACKGROUND, sp[3].zw);
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
    nextColor[0] = texture(glfx_BACKGROUND, sp[0].xy);
    nextColor[1] = texture(glfx_BACKGROUND, sp[1].xy);
    nextColor[2] = texture(glfx_BACKGROUND, sp[2].xy);
    nextColor[3] = texture(glfx_BACKGROUND, sp[3].xy);
    vec4 nextIntensity = getLuminance4(nextColor);
    vec4 curr = 0.367 * getWeight(intensity, nextIntensity);
    summ += dot(curr, vec4(1.0));
    screenColor += nextColor * curr;
    screenColor = screenColor / summ;
    
    screenColor = mix(originalColor, screenColor, factor);
    return screenColor;
}

uniform sampler2D tex_makeup, tex_makeup2;

float softlight_blend_1ch(float a, float b)
{
   return ((1.-2.*b)*a+2.*b)*a;
}

vec3 softlight_blend_1ch(vec3 base, vec3 blend) {
    return vec3(softlight_blend_1ch(base.r,blend.r),softlight_blend_1ch(base.g,blend.g),softlight_blend_1ch(base.b,blend.b));
}

vec3 softlight_blend_1ch(vec3 base, vec3 blend, float opacity) {
    return (softlight_blend_1ch(base, blend) * opacity + base * (1.0 - opacity));
}

void main()
{
    vec4 maskColor = texture(selection_tex, var_uv);
    vec4 res = texture( glfx_BACKGROUND, var_bg_uv );
    
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
    res = whitening(res, maskColor.b * eyesWhiteningCoeff, lookupTexEyes);
#endif
 
#if defined(EYES_HIGHLIGHT)
    res = res + vec4( texture( tex_highlight, var_uv ).xyz, 0. );
#endif

    vec2 uvh = var_uv;
    uvh.x = abs(2.0 * (uvh.x - 0.5));

    res.xyz = softlight_blend_1ch( res.xyz, texture( tex_makeup2, uvh ).xyz );
    
    F = vec4(res.xyz , 1.0);


}
