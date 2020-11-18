#version 300 es

precision highp float;

in vec2 var_uv;

layout( location = 0 ) out vec4 frag_color;

uniform sampler2D glfx_VIDEO;

void main()
{
	vec2 uv = var_uv;
	uv.x *= 0.5;
	vec3 rgb = texture(glfx_VIDEO,uv).xyz;
	uv.x += 0.5;
	float a = texture(glfx_VIDEO,uv).x;
	frag_color = vec4(rgb,a);
}

