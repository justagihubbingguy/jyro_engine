#version 400 core

out vec4 out_Color;

uniform vec3 sunColor;      // Base sun color
uniform vec3 cameraPos;     // Camera world position
uniform float glowRadius;   // Controls halo size
uniform vec3 sunPosition;

void main() {
    vec3 toSun = sunPosition - cameraPos;
    float dist = length(toSun);

    float core = 1.0;

    float halo = exp(-dist / glowRadius);

    float intensity = core + halo;
    out_Color = vec4(sunColor * intensity, 1.0);
}
