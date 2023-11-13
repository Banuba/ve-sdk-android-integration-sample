#include <bnb/glsl.vert>

BNB_LAYOUT_LOCATION(0)
BNB_IN vec3 attrib_pos;
BNB_LAYOUT_LOCATION(1)
BNB_IN vec3 attrib_pos_static;
BNB_LAYOUT_LOCATION(2)
BNB_IN vec2 attrib_uv;
BNB_LAYOUT_LOCATION(3)
BNB_IN vec4 attrib_red_mask;

BNB_OUT(0)
vec2 var_uv;
BNB_OUT(1)
vec3 var_red_mask;

void main()
{
    gl_Position = bnb_MVP * vec4(attrib_pos, 1.);

    var_uv = (gl_Position.xy / gl_Position.w) * 0.5 + 0.5;

#ifdef BNB_VK_1
    var_uv.y = 1. - var_uv.y;
#endif

    var_red_mask = attrib_red_mask.xyz;
}