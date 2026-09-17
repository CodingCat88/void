#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D DepthSampler;
uniform vec3 CameraPosition;
uniform mat4 InverseTransformMatrix;
uniform ivec4 ViewPort;
// Deliberately not named "Time" - vanilla's post-shader engine appears to
// drive a uniform with that exact name internally, which was overriding our
// own value and causing the animation to periodically reset.
uniform float SwirlTime;

in vec2 texCoord;
out vec4 fragColor;

// Reconstructs eye-space position from window-space depth.
// Adapted from https://www.khronos.org/opengl/wiki/Compute_eye_space_from_window_space
vec4 calcEyeFromWindow(float depth) {
    vec3 ndcPos;
    ndcPos.xy = ((2.0 * gl_FragCoord.xy) - (2.0 * ViewPort.xy)) / vec2(ViewPort.zw) - 1.0;
    ndcPos.z = (2.0 * depth - gl_DepthRange.near - gl_DepthRange.far) / (gl_DepthRange.far - gl_DepthRange.near);
    vec4 clipPos = vec4(ndcPos, 1.0);
    vec4 homogeneous = InverseTransformMatrix * clipPos;
    return vec4(homogeneous.xyz / homogeneous.w, homogeneous.w);
}

void main() {
    vec2 center = vec2(0.5);
    float dist = length(texCoord - center);

    // Distortion that grows toward the screen edges. Kept modest and the
    // sample coordinate clamped to [0,1] so the rotating offset can never
    // sample past the edge of the frame (which produced a seam/smear that
    // cycled with the rotation and looked like a "snap").
    float edgeFactor = smoothstep(0.1, 0.65, dist);
    float angle = dist * 2.2 + SwirlTime * 0.6;
    float swirlAmount = 0.035 * pow(edgeFactor, 1.6);
    vec2 swirlOffset = vec2(cos(angle), sin(angle)) * swirlAmount;
    vec2 sampleCoord = clamp(texCoord + swirlOffset, vec2(0.001), vec2(0.999));

    vec4 color = texture(DiffuseSampler, sampleCoord);

    // Reconstruct true world-space distance from the camera at this pixel,
    // so sky / far geometry can be told apart from legitimate terrain
    // (raw depth values compress almost indistinguishably close to 1.0
    // near the far plane, which is why a plain depth check only caught
    // the sun).
    float sceneDepth = texture(DepthSampler, sampleCoord).r;
    vec3 pixelPosition = calcEyeFromWindow(sceneDepth).xyz + CameraPosition;
    float distFromCamera = distance(pixelPosition, CameraPosition);
    float voidness = smoothstep(400.0, 900.0, distFromCamera);

    // Desaturate, then push what's left toward a deep, dim void-blue.
    float luminance = dot(color.rgb, vec3(0.299, 0.587, 0.114));
    vec3 desaturated = mix(color.rgb, vec3(luminance), 0.5);
    vec3 tinted = mix(desaturated, vec3(0.015, 0.025, 0.09), 0.65);
    tinted *= 0.4;

    // Vignette: darken further toward the edges.
    float vignette = smoothstep(0.9, 0.3, dist);
    tinted *= mix(0.25, 1.0, vignette);

    // Sky and far-distance geometry fade into a near-black void instead of
    // just being tinted like everything else.
    float drift = sin(sampleCoord.x * 12.0 + SwirlTime * 0.15) * sin(sampleCoord.y * 9.0 - SwirlTime * 0.1);
    vec3 voidColor = vec3(0.006, 0.008, 0.02) + drift * vec3(0.01, 0.015, 0.03);
    tinted = mix(tinted, voidColor, voidness);

    // Slow "breathing" pulse.
    float pulse = 0.5 + 0.5 * sin(SwirlTime * 0.9);
    tinted = mix(color.rgb, tinted, 0.85 + 0.08 * pulse);

    fragColor = vec4(tinted, color.a);
}
