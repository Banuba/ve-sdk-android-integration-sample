#include <bnb/glsl.frag>

BNB_IN(0) vec2 var_uv;

#ifdef USE_BG_NN
    BNB_IN(1) vec2 var_bg_mask_uv;
    BNB_DECLARE_SAMPLER_2D(2, 3, s_segmentation_mask);
    BNB_DECLARE_SAMPLER_2D(4, 5, s_bg_texture);
#endif

BNB_DECLARE_SAMPLER_2D(0, 1, tex_src);

#ifdef USE_BG_NN
vec4 cubic(float v)
{
    vec4 n = vec4(1.0, 2.0, 3.0, 4.0) - v;
    vec4 s = n * n * n;
    float x = s.x;
    float y = s.y - 4.0 * s.x;
    float z = s.z - 4.0 * s.y + 6.0 * s.x;
    float w = 6.0 - x - y - z;
    return vec4(x, y, z, w) * (1.0/6.0);
}

vec2 rgb_hs( vec3 rgb )
{
    float cmax = max(rgb.r, max(rgb.g, rgb.b));
    float cmin = min(rgb.r, min(rgb.g, rgb.b));
    float delta = cmax - cmin;
    vec2 hs = vec2(0.);
    if( cmax > cmin )
    {
        hs.y = delta/cmax;
        if( rgb.r == cmax )
            hs.x = (rgb.g-rgb.b)/delta;
        else
        {
            if( rgb.g == cmax )
                hs.x = 2.+(rgb.b-rgb.r)/delta;
            else
                hs.x = 4.+(rgb.r-rgb.g)/delta;
        }
        hs.x = fract(hs.x/6.);
    }
    return hs;
}

float rgb_v( vec3 rgb )
{
    return max(rgb.r, max(rgb.g, rgb.b));
}

vec3 hsv_rgb( float h, float s, float v )
{
    return v*mix(vec3(1.),clamp(abs(fract(vec3(1.,2./3.,1./3.)+h)*6.-3.)-1.,0.,1.),s);
}

vec3 blendColor(vec3 base, vec3 blend)
{
    float v = rgb_v( base );
    vec2 hs = rgb_hs( blend );
    return hsv_rgb( hs.x, hs.y, v );
}
    
vec3 blendColor(vec3 base, vec3 blend, float opacity)
{
    return (blendColor(base, blend) * opacity + base * (1.0 - opacity));
}
#endif
void main()
{   
    vec2 uv = var_uv;
    
    vec3 rgb = BNB_TEXTURE_2D(BNB_SAMPLER_2D(tex_src), uv).rgb;
    
    float a = 0.8;
    rgb *= a;
#ifdef USE_BG_NN
    const float threshold = 0.2;

    vec2 texSize = background_nn_meta.xy;
    vec2 invTexSize = 1.0 / texSize;
    
    vec2 texCoords = var_bg_mask_uv * texSize - 0.5;

    vec2 fxy = fract(texCoords);
    texCoords -= fxy;

    vec4 xcubic = cubic(fxy.x);
    vec4 ycubic = cubic(fxy.y);

    vec4 c = texCoords.xxyy + vec2(-0.5, +1.5).xyxy;

    vec4 s = vec4(xcubic.xz + xcubic.yw, ycubic.xz + ycubic.yw);
    vec4 offset = c + vec4(xcubic.yw, ycubic.yw) / s;

    offset *= invTexSize.xxyy;
    
    vec4 sample0 = BNB_TEXTURE_2D(BNB_SAMPLER_2D(s_segmentation_mask), offset.xz);
    vec4 sample1 = BNB_TEXTURE_2D(BNB_SAMPLER_2D(s_segmentation_mask), offset.yz);
    vec4 sample2 = BNB_TEXTURE_2D(BNB_SAMPLER_2D(s_segmentation_mask), offset.xw);
    vec4 sample3 = BNB_TEXTURE_2D(BNB_SAMPLER_2D(s_segmentation_mask), offset.yw);

    float sx = s.x / (s.x + s.y);
    float sy = s.z / (s.z + s.w);

    vec4 filtered_mask = mix(mix(sample3, sample2, sx), mix(sample1, sample0, sx), sy);
    rgb = mix(BNB_TEXTURE_2D(BNB_SAMPLER_2D(s_bg_texture), var_uv).rgb, rgb, filtered_mask.r);
    rgb *= filtered_mask.r;
    a *= filtered_mask.r;
#endif
    bnb_FragColor = vec4(rgb, a);
}