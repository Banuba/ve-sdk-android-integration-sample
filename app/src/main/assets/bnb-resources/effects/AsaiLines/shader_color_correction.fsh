precision highp float;

uniform sampler2D inputImageTexture;
uniform sampler2D lookupTexture;

varying highp vec2 textureCoordinate;

vec4 mapColor(vec4 orgColor)
{
    const float EPS = 0.000001;
    const float pxSize = 512.0;
    
    float bValue = (orgColor.b * 255.0) / 4.0;
    
    vec2 mulB = clamp(floor(bValue) + vec2(0.0, 1.0), 0.0, 63.0);
    vec2 row = floor(mulB / 8.0 + EPS);
    vec4 row_col = vec4(row, mulB - row * 8.0);
    vec4 lookup = orgColor.ggrr * (63.0/pxSize) + row_col * (64.0/pxSize) + (0.5/pxSize);
    
    float b1w = bValue - mulB.x;
    
    vec3 sampled1 = texture2D(lookupTexture, lookup.zx).rgb;
    vec3 sampled2 = texture2D(lookupTexture, lookup.wy).rgb;
    
    vec3 res = mix(sampled1, sampled2, b1w);
    return vec4(res, orgColor.a);
}

void main()
{
    vec4 originalColor = texture2D(inputImageTexture, textureCoordinate);
    gl_FragColor = mapColor(originalColor);
}
