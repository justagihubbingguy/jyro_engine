#version 330 core

in vec3 fragPos;
in vec3 fragNormal;

out vec4 FragColor;

uniform vec3 cameraPos;
uniform vec3 sunDir;

uniform float planetRadius;
uniform float atmosphereRadius;

uniform float densityFalloff;
uniform int   numSteps;
uniform float intensity;
uniform vec3  surfaceColor;



float raySphere(vec3 ro, vec3 rd, float radius) {
    float b = dot(ro, rd);
    float c = dot(ro, ro) - radius * radius;
    float h = b * b - c;
    if (h < 0.0) return -1.0;
    return -b + sqrt(h);
}

void main() {
    vec3 viewDir = normalize(fragPos - cameraPos);

    float tMax = raySphere(cameraPos, viewDir, atmosphereRadius);
    if (tMax < 0.0) discard;

    float stepSize = tMax / float(numSteps);
    float t = 0.0;

    float opticalDepth = 0.0;

    for (int i = 0; i < numSteps; i++) {
        vec3 samplePos = cameraPos + viewDir * (t + stepSize * 0.5);
        float height = length(samplePos) - planetRadius;

        if (height < 0.0) break;

        float density = exp(-height * densityFalloff);
        opticalDepth += density * stepSize;

        t += stepSize;
    }

    vec3 normal = normalize(fragPos);
    float viewAngle = dot(normal, -viewDir);
    float rim = pow(1.0 - clamp(viewAngle, 0.0, 1.0), 3.0);

    float sunAmount = max(dot(normal, normalize(sunDir)), 0.0);

    vec3 atmosphere =
    opticalDepth *
    rim *
    sunAmount *
    intensity *
    surfaceColor;

    FragColor = vec4(atmosphere, 1.0);
}
