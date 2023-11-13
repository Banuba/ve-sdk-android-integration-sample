#include <bnb/glsl.frag>

BNB_IN(0) vec2 var_uv;

BNB_DECLARE_SAMPLER_2D(0, 1, tex);

void main()
{
	float inv_sz = 1./float(textureSize(BNB_SAMPLER_2D(tex),0).x);
	vec4 sum = textureLod( BNB_SAMPLER_2D(tex), var_uv, 0. ) * 0.18;
	sum += textureLod( BNB_SAMPLER_2D(tex), var_uv + vec2(1.444444 * inv_sz, 0.0), 0. ) * 0.27;
	sum += textureLod( BNB_SAMPLER_2D(tex), var_uv - vec2(1.444444 * inv_sz, 0.0), 0. ) * 0.27;
	sum += textureLod( BNB_SAMPLER_2D(tex), var_uv + vec2(3.357143 * inv_sz, 0.0), 0. ) * 0.14;
	sum += textureLod( BNB_SAMPLER_2D(tex), var_uv - vec2(3.357143 * inv_sz, 0.0), 0. ) * 0.14;
	bnb_FragColor = sum;
}
