#version 300 es

precision highp sampler2DArray;

#define GLFX_IBL
#define GLFX_TBN
#define GLFX_TEX_MRAO
#define GLFX_LIGHTING

layout( location = 0 ) in vec3 attrib_pos;
#ifdef GLFX_LIGHTING
layout( location = 1 ) in vec3 attrib_n;
#ifdef GLFX_TBN
layout( location = 2 ) in vec4 attrib_t;
#endif
#endif
layout( location = 3 ) in vec2 attrib_uv;
layout( location = 4 ) in uvec4 attrib_bones;
#ifndef GLFX_1_BONE
layout( location = 5 ) in vec4 attrib_weights;
#endif

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

#ifdef GLFX_USE_UVMORPH
uniform sampler2D glfx_UVMORPH;
#ifdef GLFX_USE_BG
uniform sampler2D glfx_STATICPOS;
#endif
#endif

#ifdef GLFX_USE_BG
out vec2 var_bg_uv;
#endif

#ifdef GLFX_USE_AUTOMORPH
uniform sampler2D glfx_MORPH;
vec2 glfx_morph_coord( vec3 v )
{
    const float half_angle = radians(104.);
    const float y0 = -110.;
    const float y1 = 112.;
    float x = atan( v.x, v.z )/half_angle;
    float y = ((v.y-y0)/(y1-y0))*2. - 1.;
    return vec2(x,y);
}
#ifndef GLFX_AUTOMORPH_BONE
vec3 glfx_auto_morph( vec3 v )
{
    vec2 morph_uv = glfx_morph_coord(v)*0.5 + 0.5;
    vec3 translation = texture( glfx_MORPH, morph_uv ).xyz;
    return v + translation;
}
#else
vec3 glfx_auto_morph_bone( vec3 v, mat3x4 m )
{
    vec2 morph_uv = glfx_morph_coord(vec3(m[0][3],m[1][3],m[2][3]))*0.5 + 0.5;
    vec3 translation = texture( glfx_MORPH, morph_uv ).xyz;
    return v + translation;
}
#endif
#endif

out vec2 var_uv;
#ifdef GLFX_LIGHTING
#ifdef GLFX_TBN
out vec3 var_t;
out vec3 var_b;
#endif
out vec3 var_n;
out vec3 var_v;
#endif

#ifdef GLFX_USE_SHADOW
out vec3 var_shadow_coord;
vec3 spherical_proj( vec2 fovM, vec2 fovP, float zn, float zf, vec3 v )
{
    vec2 xy = (atan( v.xy, v.zz )-(fovP+fovM)*0.5)/((fovP-fovM)*0.5);
    float z = (length(v)-(zn+zf)*0.5)/((zf-zn)*0.5);
    return vec3( xy, z );
}
#endif

mat3x4 get_bone( uint bone_idx, float k )
{
    float bx = float( int(bone_idx)*3 );
    vec2 rts = 1./vec2(textureSize(glfx_BONES,0));
    return mat3x4( 
        texture( glfx_BONES, (vec2(bx,k)+0.5)*rts ),
        texture( glfx_BONES, (vec2(bx+1.,k)+0.5)*rts ),
        texture( glfx_BONES, (vec2(bx+2.,k)+0.5)*rts ) );
}

void main()
{
    mat3x4 m = get_bone( attrib_bones[0], glfx_ANIMKEY );
#ifndef GLFX_1_BONE
    if( attrib_weights[1] > 0. )
    {
        m = m*attrib_weights[0] + get_bone( attrib_bones[1], glfx_ANIMKEY )*attrib_weights[1];
        if( attrib_weights[2] > 0. )
        {
            m += get_bone( attrib_bones[2], glfx_ANIMKEY )*attrib_weights[2];
            if( attrib_weights[3] > 0. )
                m += get_bone( attrib_bones[3], glfx_ANIMKEY )*attrib_weights[3];
        }
    }
#endif

    vec3 vpos = attrib_pos;

#ifdef GLFX_USE_UVMORPH
    vec2 flip_uv = vec2( attrib_uv.x, 1. - attrib_uv.y );
    vec3 translation = texture(glfx_UVMORPH,flip_uv).xyz;
#ifdef GLFX_UVMORPH_Z_UP
    vpos += vec3(translation.x,-translation.z,translation.y);
#else
    vpos += translation;
#endif
#endif

    vpos = vec4(vpos,1.)*m;

#ifdef GLFX_USE_AUTOMORPH
#ifndef GLFX_AUTOMORPH_BONE
    vpos = glfx_auto_morph( vpos );
#else
    vpos = glfx_auto_morph_bone( vpos, m );
#endif
#endif

    gl_Position = glfx_MVP * vec4(vpos,1.);

#ifdef GLFX_USE_BG
#ifdef GLFX_USE_UVMORPH
    vec4 uvmorphed_view = glfx_MVP * vec4( texture(glfx_STATICPOS,flip_uv).xyz + translation, 1. );
    var_bg_uv = (uvmorphed_view.xy/uvmorphed_view.w)*0.5 + 0.5;
#else
    var_bg_uv = (gl_Position.xy/gl_Position.w)*0.5 + 0.5;
#endif
#endif

    var_uv = attrib_uv;

#ifdef GLFX_LIGHTING
    var_n = mat3(glfx_MV)*(attrib_n*mat3(m));
#ifdef GLFX_TBN
    var_t = mat3(glfx_MV)*(attrib_t.xyz*mat3(m));
    var_b = attrib_t.w*cross( var_n, var_t );
#endif
    var_v = (glfx_MV*vec4(vpos,1.)).xyz;
#endif

#ifdef GLFX_USE_SHADOW
    var_shadow_coord = spherical_proj(
        vec2(-radians(60.),-radians(20.)),vec2(radians(60.),radians(100.)),
        400.,70.,
        vpos+vec3(0.,100.,50.))*0.5+0.5;
#endif
}