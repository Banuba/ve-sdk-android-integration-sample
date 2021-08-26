#version 300 es

precision highp float;

in vec2 var_uv;
in vec2 var_bgmask_uv;

layout( location = 0 ) out vec4 F;

uniform sampler2D tex;
uniform sampler2D glfx_VIDEO;
uniform sampler2D glfx_BACKGROUND;
uniform sampler2D glfx_BG_MASK;

layout(std140) uniform glfx_GLOBAL
{
	mat4 glfx_MVP;
	mat4 glfx_PROJ;
	mat4 glfx_MV;
	vec4 glfx_VIEW_QUAT;

	vec4 js_media_type;
    vec4 js_angle;
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

vec4 cubic(float v) {
    vec4 n = vec4(1.0, 2.0, 3.0, 4.0) - v;
    vec4 s = n * n * n;
    float x = s.x;
    float y = s.y - 4.0 * s.x;
    float z = s.z - 4.0 * s.y + 6.0 * s.x;
    float w = 6.0 - x - y - z;
    return vec4(x, y, z, w) * (1.0 / 6.0);
}

vec2 rotate_uv_tex(vec2 uv,float angle)
{
    float c = cos(radians(angle));
    float s = sin(radians(angle));

    vec2 texSize = vec2(textureSize(tex, 0));
    float texture_aspect_ratio = texSize.y / texSize.x;
    
    uv = vec2(mat3(c, -s, 0., s, c, 0., 0.5, 0.5, 1.0) * vec3(uv - 0.5, 1.));
    
    return uv;
}

vec2 rotate_uv_video(vec2 uv,float angle)
{
    float c = cos(radians(angle));
    float s = sin(radians(angle));

    vec2 texSize = vec2(textureSize(glfx_VIDEO, 0));
    float texture_aspect_ratio = texSize.y / texSize.x;
    
    uv = vec2(mat3(c, -s, 0., s, c, 0., 0.5, 0.5, 1.0) * vec3(uv - 0.5, 1.));
    
    return uv;
}

vec2 scale_uv_tex(vec2 uv, float angle)
{
    vec2 texSize = vec2(textureSize(tex, 0));
    float aspect_ratio = glfx_SCREEN.y / glfx_SCREEN.x;
    float texture_aspect_ratio = texSize.y / texSize.x;
    float scale_x = 1.0;
    float scale_y = 1.0;

    if (angle == 90. || angle == -90. || angle == 270. || angle == -270.){
        if (texture_aspect_ratio < 1. && texture_aspect_ratio > aspect_ratio) {
            scale_x = 1. / texture_aspect_ratio / aspect_ratio;
        } else {
            scale_x =  1./ aspect_ratio / texture_aspect_ratio;

            if (scale_x < 1.) {
                scale_y /= scale_x;
                scale_x /= scale_x;
            }
        } 
    } else {
        if (texture_aspect_ratio >= aspect_ratio) {
            scale_y = texture_aspect_ratio / aspect_ratio;
        } else {
            scale_x = aspect_ratio / texture_aspect_ratio;
        }
    }

    float inv_scale_x = 1. / scale_x;
    float inv_scale_y = 1. / scale_y;
    
    return vec2(mat3(inv_scale_x, 0., 0., 0., inv_scale_y, 0., 0.5, 0.5, 1.0) * vec3(uv - 0.5, 1.));
}

vec2 scale_uv_video(vec2 uv,float angle)
{
    vec2 texSize = vec2(textureSize(glfx_VIDEO, 0));
    float aspect_ratio = glfx_SCREEN.y / glfx_SCREEN.x;
    float texture_aspect_ratio = texSize.y / texSize.x;
    float scale_x = 1.0;
    float scale_y = 1.0;

    if (angle == 90. || angle == -90. || angle == 270. || angle == -270.){
        if (texture_aspect_ratio < 1. && texture_aspect_ratio > aspect_ratio) {
            scale_x = 1. / texture_aspect_ratio / aspect_ratio;
        } else {
            scale_x =  1./ aspect_ratio / texture_aspect_ratio;
            if (scale_x < 1.) {
                scale_y /= scale_x;
                scale_x /= scale_x;
            }
        } 
    }
    else {
        if (texture_aspect_ratio > aspect_ratio) {
            scale_y = texture_aspect_ratio / aspect_ratio;
        } else {
            scale_x = aspect_ratio / texture_aspect_ratio;
        }
    }

    float inv_scale_x = 1. / scale_x;
    float inv_scale_y = 1. / scale_y;

    return vec2(mat3(inv_scale_x, 0., 0., 0., inv_scale_y, 0., 0.5, 0.5, 1.0) * vec3(uv - 0.5, 1.));
}

vec4 textureBicubic(sampler2D sampler, vec2 texCoords){
    vec2 texSize = vec2(textureSize(sampler, 0));
    vec2 invTexSize = 1.0 / texSize;

    texCoords = texCoords * texSize - 0.5;

    vec2 fxy = fract(texCoords);
    texCoords -= fxy;

    vec4 xcubic = cubic(fxy.x);
    vec4 ycubic = cubic(fxy.y);

    vec4 c = texCoords.xxyy + vec2(-0.5, +1.5).xyxy;

    vec4 s = vec4(xcubic.xz + xcubic.yw, ycubic.xz + ycubic.yw);
    vec4 offset = c + vec4(xcubic.yw, ycubic.yw) / s;

    offset *= invTexSize.xxyy;

    vec4 sample0 = texture(sampler, offset.xz);
    vec4 sample1 = texture(sampler, offset.yz);
    vec4 sample2 = texture(sampler, offset.xw);
    vec4 sample3 = texture(sampler, offset.yw);

    float sx = s.x / (s.x + s.y);
    float sy = s.z / (s.z + s.w);

    return mix(
        mix(sample3, sample2, sx), mix(sample1, sample0, sx)
    , sy);
}

void main()
{   
    vec2 uv = var_uv;
    vec3 bg = texture(glfx_BACKGROUND, uv).xyz;
    uv.y = 1. - uv.y;
    vec4 bg_tex = vec4(0.);
    float angle = js_angle.x;

    if(js_media_type.x == 1.){
        if (angle != 0.) uv = rotate_uv_tex(uv,angle);
        uv = scale_uv_tex(uv,angle);
        
        bg_tex = texture(tex, uv);
    } else {
        if (angle != 0.) uv = rotate_uv_video(uv,angle);
        uv = scale_uv_video(uv,angle);

        bg_tex = texture(glfx_VIDEO, uv);
    }
    const float threshold = 0.2;
    float mask = max((textureBicubic(glfx_BG_MASK,var_bgmask_uv).x - threshold) / (1.0 - threshold), 0.0);

    F = vec4(mix(bg, bg_tex.xyz, mask), 1.0);
}
