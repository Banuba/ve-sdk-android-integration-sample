#version 300 es

layout( location = 0 ) in vec3 attrib_pos;
layout( location = 1 ) in vec2 attrib_uv;

layout(std140) uniform glfx_GLOBAL
{
	mat4 glfx_MVP;
	mat4 glfx_PROJ;
	mat4 glfx_MV;
};
layout(std140) uniform glfx_INSTANCES
{
	vec4 glfx_IDATA[8];
};
uniform uint glfx_CURRENT_I;
#define glfx_T_SPAWN (glfx_IDATA[glfx_CURRENT_I].x)
#define glfx_T_ANIM (glfx_IDATA[glfx_CURRENT_I].y)
#define glfx_ANIMKEY (glfx_IDATA[glfx_CURRENT_I].z)

out vec2 var_uv;
out vec2 var_bg_uv;

invariant gl_Position;

void main()
{
	gl_Position = glfx_MVP * vec4( attrib_pos, 1. );
	var_uv = attrib_uv;
    var_bg_uv  = (gl_Position.xy/gl_Position.w) * 0.5 + 0.5;
}
