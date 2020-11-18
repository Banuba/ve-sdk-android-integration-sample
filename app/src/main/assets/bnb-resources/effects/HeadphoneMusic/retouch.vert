#version 300 es

layout( location = 0 ) in vec3 attrib_pos;
layout( location = 1 ) in vec2 attrib_uv;

layout(std140) uniform glfx_GLOBAL
{
    mat4 glfx_MVP;
    mat4 glfx_PROJ;
    mat4 glfx_MV;
};

out vec2 var_uv;
out vec2 var_bg_uv;

out mat4 sp;

invariant gl_Position;

const float dx = 1.0 / 960.0;
const float dy = 1.0 / 1280.0;

const float delta = 5.;

const float sOfssetXneg = -delta * dx;
const float sOffsetYneg = -delta * dy;
const float sOffsetXpos = delta * dx;
const float sOffsetYpos = delta * dy;

void main()
{
    gl_Position = glfx_MVP * vec4( attrib_pos, 1. );
    var_uv = attrib_uv;
    var_bg_uv  = (gl_Position.xy / gl_Position.w) * 0.5 + 0.5;
    
    sp[0].xy = var_bg_uv + vec2(sOfssetXneg, sOffsetYneg);
    sp[1].xy = var_bg_uv + vec2(sOfssetXneg, sOffsetYpos);
    sp[2].xy = var_bg_uv + vec2(sOffsetXpos, sOffsetYneg);
    sp[3].xy = var_bg_uv + vec2(sOffsetXpos, sOffsetYpos);
    
    vec2 delta = vec2(dx, dy);
    sp[0].zw = var_bg_uv + vec2(-delta.x, -delta.y);
    sp[1].zw = var_bg_uv + vec2(delta.x, -delta.y);
    sp[2].zw = var_bg_uv + vec2(-delta.x, delta.y);
    sp[3].zw = var_bg_uv + vec2(delta.x, delta.y);
}
