#include <bnb/glsl.frag>




BNB_IN(0) vec4 var_uv;



BNB_DECLARE_SAMPLER_2D(4, 5, glfx_BACKGROUND);

BNB_DECLARE_SAMPLER_2D(0, 1, glfx_LIPS_MASK);

BNB_DECLARE_SAMPLER_2D(2, 3, glfx_LIPS_SHINE_MASK);

const float eps = 0.0000001;

vec3 hsv2rgb( in vec3 c )
{
    vec3 rgb = clamp( abs( mod( c.x * 6.0 + vec3(0.0,4.0,2.0), 6.0 ) - 3.0 ) - 1.0, 0.0, 1.0 );
	return c.z * mix( vec3(1.0), rgb, c.y );
}

vec3 rgb2hsv( in vec3 c )
{
    vec4 k = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);
    vec4 p = mix( vec4(c.zy, k.wz), vec4(c.yz, k.xy), (c.z < c.y) ? 1.0 : 0.0 );
    vec4 q = mix( vec4(p.xyw, c.x), vec4(c.x, p.yzx), (p.x < c.x) ? 1.0 : 0.0 );
    float d = q.x - min( q.w, q.y );
    return vec3(abs( q.z + (q.w - q.y) / (6.0 * d + eps) ), d / (q.x+eps), q.x );
}

vec4 cubic(float v)
{
    vec4 n = vec4(1.0, 2.0, 3.0, 4.0) - v;
    vec4 s = n * n * n;
    float x = s.x;
    float y = s.y - 4.0 * s.x;
    float z = s.z - 4.0 * s.y + 6.0 * s.x;
    float w = 6.0 - x - y - z;
    return vec4(x, y, z, w) * (1.0/6.0);
}

vec2 rgb_hs( vec3 rgb )
{
    float cmax = max(rgb.r, max(rgb.g, rgb.b));
    float cmin = min(rgb.r, min(rgb.g, rgb.b));
    float delta = cmax - cmin;
    vec2 hs = vec2(0.);
    if( cmax > cmin )
    {
        hs.y = delta/cmax;
        if( rgb.r == cmax )
            hs.x = (rgb.g-rgb.b)/delta;
        else
        {
            if( rgb.g == cmax )
                hs.x = 2.+(rgb.b-rgb.r)/delta;
            else
                hs.x = 4.+(rgb.r-rgb.g)/delta;
        }
        hs.x = fract(hs.x/6.);
    }
    return hs;
}

vec4 gaussianBlur(vec2 uv){
    float Pi = 6.28318530718; // Pi*2
    
    // GAUSSIAN BLUR SETTINGS {{{
    float Directions = 16.0; // BLUR DIRECTIONS (Default 16.0 - More is better but slower)
    float Quality = 4.0; // BLUR QUALITY (Default 4.0 - More is better but slower)
    float Size = 8.0; // BLUR SIZE (Radius)
    // GAUSSIAN BLUR SETTINGS }}}
   
    vec2 Radius = Size/bnb_SCREEN.xy;
    
    // Pixel colour
    vec4 Color =  BNB_TEXTURE_2D(BNB_SAMPLER_2D(glfx_BACKGROUND), uv );
    
    // Blur calculations
    for( float d=0.0; d<Pi; d+=Pi/Directions)
    {
		for(float i=1.0/Quality; i<=1.0; i+=1.0/Quality)
        {
			Color += BNB_TEXTURE_2D(BNB_SAMPLER_2D(glfx_BACKGROUND), uv+vec2(cos(d),sin(d))*Radius*i);		
        }
    }
    
    // Output to screen
    Color /= Quality * Directions - 15.0;
    return Color;
}

void main()
{
	vec4 maskColor = BNB_TEXTURE_2D(BNB_SAMPLER_2D(glfx_LIPS_MASK), var_uv.zw );
	float maskAlpha = maskColor[int(lips_nn_transform[0].w)] * js_color.w;

	vec3 bg = gaussianBlur(var_uv.xy).xyz;

    const float threshold = 0.2;

    vec2 texSize = background_nn_meta.xy;
    vec2 invTexSize = 1.0 / texSize;

    vec2 texCoords = var_uv.zw * texSize - 0.5;

    vec2 fxy = fract(texCoords);
    texCoords -= fxy;

    vec4 xcubic = cubic(fxy.x);
    vec4 ycubic = cubic(fxy.y);

    vec4 c = texCoords.xxyy + vec2(-0.5, +1.5).xyxy;

    vec4 s = vec4(xcubic.xz + xcubic.yw, ycubic.xz + ycubic.yw);
    vec4 offset = c + vec4(xcubic.yw, ycubic.yw) / s;

    offset *= invTexSize.xxyy;

    vec4 sample0 = BNB_TEXTURE_2D(BNB_SAMPLER_2D(glfx_LIPS_MASK), offset.xz);
    vec4 sample1 = BNB_TEXTURE_2D(BNB_SAMPLER_2D(glfx_LIPS_MASK), offset.yz);
    vec4 sample2 = BNB_TEXTURE_2D(BNB_SAMPLER_2D(glfx_LIPS_MASK), offset.xw);
    vec4 sample3 = BNB_TEXTURE_2D(BNB_SAMPLER_2D(glfx_LIPS_MASK), offset.yw);

    float sx = s.x / (s.x + s.y);
    float sy = s.z / (s.z + s.w);

    vec4 filtered_mask = mix(mix(sample3, sample2, sx), mix(sample1, sample0, sx), sy);

    float mask = max((filtered_mask.x - threshold)/(1. - threshold),0.);



	// Lipstick
	float sCoef = params.x;;

	vec3 js_color_hsv = rgb2hsv( js_color.rgb );
	vec3 bg_color_hsv = rgb2hsv( bg );

	float color_hsv_s = js_color_hsv.g * sCoef;
	if ( sCoef > 1. ) {
		color_hsv_s = js_color_hsv.g + (1. - js_color_hsv.g) * (sCoef - 1.);
	}

	float vCoef = params.y;
	float sCoef1 = params.z;
	float bCoef = params.w;
	float a = 20.;
	float b = .75;

	vec3 color_lipstick = vec3(
		js_color_hsv.r,
		color_hsv_s,
		bg_color_hsv.b);

	vec3 color_lipstick_b = color_lipstick * vec3(1., 1., bCoef);
	vec3 color = maskAlpha * hsv2rgb( color_lipstick_b ) + (1. - maskAlpha) * bg;

	// Shine
	vec4 shineColor = BNB_TEXTURE_2D(BNB_SAMPLER_2D(glfx_LIPS_SHINE_MASK), var_uv.zw );
	float shineAlpha = 1.0 * shineColor[int(lips_nn_transform[0].w)] * js_color.w;

	float v_min = lips_shining_shine_params.x;
	float v_max = lips_shining_shine_params.y;

	float x = (color_lipstick.z - v_min) / (v_max - v_min);
	float y = 1. / (1. + exp( -(x - b) * a * (1. + x) ));

	float v1 = color_lipstick.z * (1. - maskAlpha) + color_lipstick.z * maskAlpha * bCoef;
	float v2 = color_lipstick.z + (1. - color_lipstick.z) * vCoef * y;
	float v3 = v1 * (1. - y) + v2 * y;

	vec3 color_shine = vec3(
		color_lipstick.x,
		color_lipstick.y * (1. - sCoef1 * y),
		v3);

	color = shineAlpha * hsv2rgb( color_shine ) + (1. - shineAlpha) * color;

	// if(js_face.x == 0.) discard;

	bnb_FragColor = vec4(color, maskAlpha * lips_blur.x);
}
