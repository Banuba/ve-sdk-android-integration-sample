#include <bnb/glsl.frag>

BNB_IN(0) vec2 var_uv;

BNB_DECLARE_SAMPLER_2D(0, 1, s_bg);
BNB_DECLARE_SAMPLER_2D(2, 3, s_segmentation_mask);

void main()
{
    vec4 c = brows_color;
    c.a *= BNB_TEXTURE_2D(BNB_SAMPLER_2D(s_segmentation_mask), var_uv)[0];
    bnb_FragColor = c;
}