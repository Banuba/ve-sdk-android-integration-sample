#include <bnb/glsl.frag>

BNB_IN(0)
vec2 var_uv;
BNB_IN(1)
vec3 var_red_mask;

#define softSkinIntensity 0.8
#define SOFTSKIN_RADIUS 0.1000
#define RETOUCH_EPSILON 0.000001

BNB_DECLARE_SAMPLER_2D(0, 1, tex_camera);

vec4 softSkin1(vec2 uv, float factor)
{
    vec4 camera = textureLod(BNB_SAMPLER_2D(tex_camera), uv, 0.);
    vec3 originalColor = camera.xyz;
    vec3 screenColor = originalColor;

    float dx = 4.5 / bnb_SCREEN.x;
    float dy = 4.5 / bnb_SCREEN.y;

    vec3 nextColor0 = textureLod(BNB_SAMPLER_2D(tex_camera), vec2(uv.x - dx, uv.y - dy), 0.).xyz;
    vec3 nextColor1 = textureLod(BNB_SAMPLER_2D(tex_camera), vec2(uv.x + dx, uv.y - dy), 0.).xyz;
    vec3 nextColor2 = textureLod(BNB_SAMPLER_2D(tex_camera), vec2(uv.x - dx, uv.y + dy), 0.).xyz;
    vec3 nextColor3 = textureLod(BNB_SAMPLER_2D(tex_camera), vec2(uv.x + dx, uv.y + dy), 0.).xyz;

    float intensity = screenColor.g;
    vec4 nextIntensity = vec4(nextColor0.g, nextColor1.g, nextColor2.g, nextColor3.g);
    vec4 lg = nextIntensity - intensity;

    const float PSI = 0.05;
    vec4 curr = max(0.367 - abs(lg * (0.367 * 0.6 / (1.41 * PSI))), 0.);

    float summ = 1.0 + curr.x + curr.y + curr.z + curr.w;
    screenColor += (nextColor0 * curr.x + nextColor1 * curr.y + nextColor2 * curr.z + nextColor3 * curr.w);
    screenColor = screenColor * (factor / summ);

    screenColor = originalColor * (1. - factor) + screenColor;
    return vec4(screenColor, camera.a);
}

vec4 getLuminance4(mat4 color){
    const vec4 rgb2y=vec4(0.299,0.587,0.114,0.0);
    return rgb2y*color;
}
float getLuminance(vec4 color){
    const vec4 rgb2y=vec4(0.299,0.587,0.114,0.0);
    return dot(color,rgb2y);
}
float rand(vec2 co){
    return fract(sin(dot(co.xy,vec2(12.9898,78.233)))*43758.5453);
}
vec4 getWeight(float intens,vec4 nextIntens){
    vec4 lg=log(nextIntens/(intens+RETOUCH_EPSILON));
    lg*=lg;
    return exp(lg*(-1.0/(2.0*SOFTSKIN_RADIUS*SOFTSKIN_RADIUS)));
}
vec4 softSkin(float factor){
    vec4 originalColor = textureLod(BNB_SAMPLER_2D(tex_camera),var_uv,0.);
    vec4 screenColor=originalColor;
    float intens=getLuminance(screenColor);
    float sum=1.0;
    mat4 nextColor;
    vec2 texCoord0 = var_uv+vec2(-0.00694444,-0.00390625);
    vec2 texCoord1 = var_uv+vec2(-0.00694444,0.00546875);
    vec2 texCoord2 = var_uv+vec2(0.00972222,-0.00390625);
    vec2 texCoord3 = var_uv+vec2(0.00972222,0.00546875);
    nextColor[0]=textureLod(BNB_SAMPLER_2D(tex_camera),texCoord0,0.);
    nextColor[1]=textureLod(BNB_SAMPLER_2D(tex_camera),texCoord1,0.);
    nextColor[2]=textureLod(BNB_SAMPLER_2D(tex_camera),texCoord2,0.);
    nextColor[3]=textureLod(BNB_SAMPLER_2D(tex_camera),texCoord3,0.);
    vec4 nextIntens=getLuminance4(nextColor);
    vec4 curr=0.36787944*getWeight(intens,nextIntens);
    sum+=dot(curr,vec4(1.0));
    screenColor+=nextColor*curr;
    float noise=(rand(var_uv) -0.5)/30.0;
    screenColor=screenColor/sum+vec4(noise,noise,noise,1.0);
    screenColor=mix(originalColor,screenColor,factor);
    return screenColor;
}

void main()
{
    float mask = var_red_mask.r * (0.8 *js_slider.x);

    vec4 softened = softSkin(mask);

    bnb_FragColor = vec4( softened);
}
