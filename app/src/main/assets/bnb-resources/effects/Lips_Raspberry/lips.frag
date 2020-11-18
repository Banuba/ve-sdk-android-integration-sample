#version 300 es

precision highp float;

layout(std140) uniform glfx_GLOBAL
{
    mat4 glfx_MVP;
    mat4 glfx_PROJ;
    mat4 glfx_MV;
    vec4 glfx_QUAT;
    vec4 js_color;
	vec4 js_on_off;
};

layout(std140) uniform glfx_BASIS_DATA
{
	vec4 unused;
	vec4 glfx_SCREEN;
	vec4 glfx_BG_MASK_T[2];
	vec4 glfx_HAIR_MASK_T[2];
	vec4 glfx_LIPS_MASK_T[2];
	vec4 glfx_L_EYE_MASK_T[2];
	vec4 glfx_R_EYE_MASK_T[2];
	vec4 glfx_SKIN_MASK_T[2];
	vec4 glfx_OCCLUSION_MASK_T[2];
};

in vec4 var_uv;

layout( location = 0 ) out vec4 F;

uniform sampler2D glfx_BACKGROUND, glfx_LIPS_MASK;

#define YUV2RGB_RED_CrV 1.402
#define YUV2RGB_GREEN_CbU 0.3441
#define YUV2RGB_GREEN_CrV 0.7141
#define YUV2RGB_BLUE_CbU 1.772

vec4 rgba2yuva (vec4 rgba)
{
	vec4 yuva = vec4(0.);

	yuva.x = rgba.r * 0.299 + rgba.g * 0.587 + rgba.b * 0.114;
	yuva.y = rgba.r * -0.169 + rgba.g * -0.331 + rgba.b * 0.5 + 0.5;
	yuva.z = rgba.r * 0.5 + rgba.g * -0.419 + rgba.b * -0.081 + 0.5;
	yuva.w = rgba.a;

	return yuva;
}

void main()
{
	float onOff = js_on_off.x;
	
	vec3 bg = texture( glfx_BACKGROUND, var_uv.xy ).xyz;
	vec4 js_yuva = rgba2yuva(js_color);
	vec2 maskColor = js_yuva.yz;
	float beta = js_yuva.x;

	float y = dot(bg, vec3(0.2989,0.5866,0.1144));

	vec2 uv_src = vec2(
		dot(bg, vec3(-0.1688,-0.3312,0.5)) + 0.5,
		dot(bg, vec3(0.5,-0.4183,-0.0816)) + 0.5);

	float alpha = (texture( glfx_LIPS_MASK, var_uv.zw )[int(glfx_LIPS_MASK_T[0].w)]) * js_yuva.w;

	vec2 uv = (1.0 - alpha) * uv_src + alpha * ((1.0 - beta) * maskColor + beta * uv_src);

	float u = uv.x - 0.5;
	float v = uv.y - 0.5;

	float r = y + YUV2RGB_RED_CrV * v;
	float g = y - YUV2RGB_GREEN_CbU * u - YUV2RGB_GREEN_CrV * v;
	float b = y + YUV2RGB_BLUE_CbU * u;

	vec4 color = vec4(r, g, b, 1.0);
	if(onOff == 1.){
		F = color;
	} else {
		F = texture( glfx_BACKGROUND, var_uv.xy );
	}
	
}
