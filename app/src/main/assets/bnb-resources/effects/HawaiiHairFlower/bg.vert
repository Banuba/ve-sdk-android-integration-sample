#version 300 es

layout( location = 0 ) in vec3 attrib_pos;

layout(std140) uniform glfx_GLOBAL
{
	mat4 glfx_MVP;
	mat4 glfx_PROJ;
	mat4 glfx_MV;
};

out vec2 var_uv;
out vec2 var_bgmask_uv;

void main()
{
	vec2 v = attrib_pos.xy;
	gl_Position = vec4( v, 1., 1. );
	var_uv = v*0.5 + 0.5;
	var_bgmask_uv = vec2(-sign(glfx_PROJ[0][0]),-sign(glfx_PROJ[1][1]))*v.yx*0.5 + 0.5;
}