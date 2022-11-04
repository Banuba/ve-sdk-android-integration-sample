#include <bnb/glsl.frag>


BNB_IN(0) vec2 var_uv;
BNB_IN(1) vec2 var_bg_uv;


BNB_DECLARE_SAMPLER_VIDEO(2, 3, glfx_VIDEO);

BNB_DECLARE_SAMPLER_2D(0, 1, glfx_BACKGROUND);

vec3 overlay_blend(vec3 bg, vec3 fg)
{
    vec3 res;
    res.x = bg.x < 0.5 ? (2.0 * bg.x * fg.x) : (1.0 - 2.0 * (1.0 - bg.x) * (1.0 - fg.x));
    res.y = bg.y < 0.5 ? (2.0 * bg.y * fg.y) : (1.0 - 2.0 * (1.0 - bg.y) * (1.0 - fg.y));
    res.z = bg.z < 0.5 ? (2.0 * bg.z * fg.z) : (1.0 - 2.0 * (1.0 - bg.z) * (1.0 - fg.z));
    return res;
}

void main()
{
	vec2 uv = var_uv;
	uv.x *= 0.5;
	vec3 rgb = BNB_TEXTURE_2D(BNB_SAMPLER_2D(glfx_VIDEO),uv).xyz;
	uv.x += 0.5;
	float a = BNB_TEXTURE_2D(BNB_SAMPLER_2D(glfx_VIDEO),uv).x;
	vec4 c = vec4(rgb,a);
//    vec4 bg_pixel = BNB_TEXTURE_2D(BNB_SAMPLER_2D(glfx_BACKGROUND), var_bg_uv);
//    c.rgb = overlay_blend(bg_pixel.rgb, c.rgb);
    
	bnb_FragColor = c;
}

