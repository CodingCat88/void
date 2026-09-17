#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D DiffuseDepthSampler;
uniform float Time;

in vec2 texCoord;
out vec4 fragColor;

void main() {
    vec2 center = vec2(0.5);
    vec2 toCenter = texCoord - center;
    float dist = length(toCenter);

    // Distortion that grows sharply toward the screen edges - barely
    // noticeable near the center, strong in the corners.
    float edgeFactor = smoothstep(0.1, 0.65, dist);
    float angle = dist * 2.2 + Time * 0.6;
    float swirlAmount = 0.07 * pow(edgeFactor, 1.6);
    vec2 swirlOffset = vec2(cos(angle), sin(angle)) * swirlAmount;

    vec2 sampleCoord = texCoord + swirlOffset;
    vec4 color = texture(DiffuseSampler, sampleCoord);
    float depth = texture(DiffuseDepthSampler, sampleCoord).r;

    // Desaturate, then push what's left toward a deep, dim void-blue.
    float luminance = dot(color.rgb, vec3(0.299, 0.587, 0.114));
    vec3 desaturated = mix(color.rgb, vec3(luminance), 0.45);
    vec3 tinted = mix(desaturated, vec3(0.015, 0.025, 0.09), 0.6);

    // Overall darkening - the Layer should feel dim, not just blue-tinted.
    tinted *= 0.55;

    // Vignette: darken further toward the edges.
    float vignette = smoothstep(0.9, 0.3, dist);
    tinted *= mix(0.3, 1.0, vignette);

    // Slow "breathing" pulse, smooth since Time is a monotonic wall-clock
    // counter driven from Java and never gets reset mid-session.
    float pulse = 0.5 + 0.5 * sin(Time * 0.9);
    tinted = mix(color.rgb, tinted, 0.85 + 0.08 * pulse);


    fragColor = vec4(tinted, color.a);
}
