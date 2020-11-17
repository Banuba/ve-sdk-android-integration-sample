#version 300 es

precision highp sampler2DArray;

layout( location = 0 ) in vec3 attrib_pos;
layout( location = 3 ) in vec2 attrib_uv;
layout( location = 4 ) in uvec4 attrib_bones;

layout(std140) uniform glfx_GLOBAL
{
	mat4 glfx_MVP;
	mat4 glfx_PROJ;
	mat4 glfx_MV;
};
layout(std140) uniform glfx_INSTANCES
{
	vec4 glfx_IDATA[48];
};
uniform uint glfx_CURRENT_I;
#define glfx_T_SPAWN (glfx_IDATA[glfx_CURRENT_I].x)
#define glfx_T_ANIM (glfx_IDATA[glfx_CURRENT_I].y)
#define glfx_ANIMKEY (glfx_IDATA[glfx_CURRENT_I].z)

uniform sampler2D glfx_BONES;

out vec2 var_uv;

mat3x4 get_bone( uint bone_idx, float k )
{
	float bx = float( int(bone_idx)*3 );
	vec2 rts = 1./vec2(textureSize(glfx_BONES,0));
	return mat3x4( 
		texture( glfx_BONES, (vec2(bx,k)+0.5)*rts ),
		texture( glfx_BONES, (vec2(bx+1.,k)+0.5)*rts ),
		texture( glfx_BONES, (vec2(bx+2.,k)+0.5)*rts ) );
}

mat3 shortest_arc_m3( vec3 from, vec3 to )
{
	vec3 a = cross( from, to );
	float c = dot( from, to );

	float t = 1./(1.+c);
	float tx = t*a.x;
	float ty = t*a.y;
	float tz = t*a.z;
	float txy = tx*a.y;
	float txz = tx*a.z;
	float tyz = ty*a.z;

	return mat3
	(
		c + tx*a.x, txy + a.z, txz - a.y,
		txy - a.z, c + ty*a.y, tyz + a.x,
		txz + a.y, tyz - a.x, c + tz*a.z
	);
}

void main()
{
	mat3x4 m = get_bone( attrib_bones[0], glfx_ANIMKEY );

	vec3 vpos = attrib_pos;

	vec3 eye = -glfx_MV[3].xyz;
	vec3 pivot = vec3(m[0].w,m[1].w,m[2].w);

	mat3 billboard_rotation = shortest_arc_m3( 
		vec3(0.,0.,1.), 
		normalize(eye-pivot)*mat3(glfx_MV) );
	vpos = vec4(billboard_rotation*vpos,1.)*m;

	gl_Position = glfx_MVP * vec4(vpos,1.);

	gl_Position.z = -1.;

	var_uv = attrib_uv;
}
