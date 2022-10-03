#include <bnb/glsl.vert>
#include <bnb/decode_int1010102.glsl>
#include<bnb/matrix_operations.glsl>
#define bnb_IDX_OFFSET 0
#ifdef BNB_VK_1
#ifdef gl_VertexID
#undef gl_VertexID
#endif
#ifdef gl_InstanceID
#undef gl_InstanceID
#endif
#define gl_VertexID gl_VertexIndex
#define gl_InstanceID gl_InstanceIndex
#endif

#define MORPH_MULTIPLIER 1.6

#define BNB_USE_AUTOMORPH
//#define BNB_1_BONE
#define GLFX_TBN
#define GLFX_LIGHTING

BNB_LAYOUT_LOCATION(0) BNB_IN vec3 attrib_pos;
#ifdef GLFX_LIGHTING
#ifdef BNB_VK_1
BNB_LAYOUT_LOCATION(1) BNB_IN uint attrib_n;
#else
BNB_LAYOUT_LOCATION(1) BNB_IN vec4 attrib_n;
#endif
#ifdef GLFX_TBN
#ifdef BNB_VK_1
BNB_LAYOUT_LOCATION(2) BNB_IN uint attrib_t;
#else
BNB_LAYOUT_LOCATION(2) BNB_IN vec4 attrib_t;
#endif
#endif
#endif
BNB_LAYOUT_LOCATION(3) BNB_IN vec2 attrib_uv;
#ifndef BNB_GL_ES_1
BNB_LAYOUT_LOCATION(4) BNB_IN uvec4 attrib_bones;
#else
BNB_LAYOUT_LOCATION(4) BNB_IN vec4 attrib_bones;
#endif
#ifndef BNB_1_BONE
BNB_LAYOUT_LOCATION(5) BNB_IN vec4 attrib_weights;
#endif



BNB_DECLARE_SAMPLER_2D(2, 3, bnb_BONES);


BNB_DECLARE_SAMPLER_2D(4, 5, bnb_MORPH);

BNB_OUT(0) vec2 var_uv;
#ifdef GLFX_LIGHTING
#ifdef GLFX_TBN
BNB_OUT(1) vec3 var_t;
BNB_OUT(2) vec3 var_b;
#endif
BNB_OUT(3) vec3 var_n;
BNB_OUT(4) vec3 var_v;
#endif

#include <bnb/morph_transform.glsl>

#ifdef BNB_GL_ES_1

mat4 get_bone_autobone(float b, float db)
{
    vec4 v0 = BNB_TEXTURE_2D(BNB_SAMPLER_2D(bnb_BONES), vec2(b, 0.));
    vec4 i0 = BNB_TEXTURE_2D(BNB_SAMPLER_2D(bnb_BONES), vec2(b, 1.));
    b += db;
    vec4 v1 = BNB_TEXTURE_2D(BNB_SAMPLER_2D(bnb_BONES), vec2(b, 0.));
    vec4 i1 = BNB_TEXTURE_2D(BNB_SAMPLER_2D(bnb_BONES), vec2(b, 1.));
    b += db;
    vec4 v2 = BNB_TEXTURE_2D(BNB_SAMPLER_2D(bnb_BONES), vec2(b, 0.));
    vec4 i2 = BNB_TEXTURE_2D(BNB_SAMPLER_2D(bnb_BONES), vec2(b, 1.));

    mat4 m = transpose( mat4(v0, v1, v2, vec4(0., 0., 0., 1.)) );

    vec2 morph_uv = bnb_morph_coord(m[3].xyz)*0.5 + 0.5;
    vec3 translation = BNB_TEXTURE_2D(BNB_SAMPLER_2D(bnb_MORPH), morph_uv).xyz;
    m[3].xyz += translation*MORPH_MULTIPLIER;

    mat4 ibp = transpose( mat4(i0, i1, i2, vec4(0., 0., 0., 1.)) );

    return m*ibp;
}

mat4 get_transform_autobone()
{
    float db = 1. / (bnb_ANIM.z * 3.);
    mat4 m = get_bone_autobone(attrib_bones[0], db);

    if (attrib_weights[1] > 0.) {
        m = m * attrib_weights[0] + get_bone_autobone(attrib_bones[1], db) * attrib_weights[1];

        if (attrib_weights[2] > 0.) {
            m += get_bone_autobone(attrib_bones[2], db) * attrib_weights[2];

            if (attrib_weights[3] > 0.) {
                m += get_bone_autobone(attrib_bones[3], db) * attrib_weights[3];
            }
        }
    }

    return m;
}

#else

mat4 get_bone_autobone(uint bone_idx)
{
    int b = int(bone_idx)*3;
    mat4 m = transpose( mat4(
        texelFetch( BNB_SAMPLER_2D(bnb_BONES), ivec2(b,0), 0 ),
        texelFetch( BNB_SAMPLER_2D(bnb_BONES), ivec2(b+1,0), 0 ),
        texelFetch( BNB_SAMPLER_2D(bnb_BONES), ivec2(b+2,0), 0 ),
        vec4(0.,0.,0.,1.) ) );

    vec2 morph_uv = bnb_morph_coord(m[3].xyz)*0.5 + 0.5;
#ifdef BNB_VK_1
    morph_uv.y = 1. - morph_uv.y;
#endif
    vec3 translation = BNB_TEXTURE_2D(BNB_SAMPLER_2D(bnb_MORPH), morph_uv).xyz;
    m[3].xyz += translation*MORPH_MULTIPLIER;

    mat4 ibp = transpose( mat4(
        texelFetch( BNB_SAMPLER_2D(bnb_BONES), ivec2(b,1), 0 ),
        texelFetch( BNB_SAMPLER_2D(bnb_BONES), ivec2(b+1,1), 0 ),
        texelFetch( BNB_SAMPLER_2D(bnb_BONES), ivec2(b+2,1), 0 ),
        vec4(0.,0.,0.,1.) ) );

    return m*ibp;
}

mat4 get_transform_autobone()
{
    mat4 m = get_bone_autobone( attrib_bones[0] );
    if( attrib_weights[1] > 0. )
    {
        m = m*attrib_weights[0] + get_bone_autobone( attrib_bones[1] )*attrib_weights[1];
        if( attrib_weights[2] > 0. )
        {
            m += get_bone_autobone( attrib_bones[2] )*attrib_weights[2];
            if( attrib_weights[3] > 0. )
                m += get_bone_autobone( attrib_bones[3] )*attrib_weights[3];
        }
    }
    return m;
}

#endif     // BNB_GL_ES_1

void main()
{
    mat4 m = get_transform_autobone();
    vec3 vpos = attrib_pos;

    vpos = (m*vec4(vpos,1.)).xyz;

    gl_Position = bnb_MVP * vec4(vpos,1.);

    var_uv = attrib_uv;

#ifdef GLFX_LIGHTING
    var_n = mat3(bnb_MV)*(mat3(m)*bnb_decode_int1010102(attrib_n).xyz);
#ifdef GLFX_TBN
    var_t = mat3(bnb_MV)*(mat3(m)*bnb_decode_int1010102(attrib_t).xyz);
    var_b = bnb_decode_int1010102(attrib_t).w*cross( var_n, var_t );
#endif
    var_v = (bnb_MV*vec4(vpos,1.)).xyz;
#endif
}
