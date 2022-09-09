#include <bnb/glsl.frag>


BNB_IN(0) vec2 var_uv;



BNB_DECLARE_SAMPLER_2D(0, 1, tex_diffuse);

void main()
{
	bnb_FragColor = BNB_TEXTURE_2D(BNB_SAMPLER_2D(tex_diffuse),var_uv);

}
