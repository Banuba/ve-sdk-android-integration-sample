#include <bnb/glsl.vert>

BNB_LAYOUT_LOCATION(0) BNB_IN vec2 attrib_pos;

BNB_OUT(0) vec2 var_uv;
BNB_OUT(1) vec2 var_mask_uv;

void main()
{
	vec2 v = attrib_pos.xy;
	gl_Position = vec4( v, 0., 1. );
	var_uv = v*0.5 + 0.5;

#ifdef BNB_VK_1
	var_uv.y = 1. - var_uv.y;
#endif

	var_mask_uv = vec2(vec4(v,1.,1.) * skin_nn_transform);
}
