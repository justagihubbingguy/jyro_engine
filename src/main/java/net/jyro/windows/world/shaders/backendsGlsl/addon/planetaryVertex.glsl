#version 400 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 texCoords;
layout(location = 2) in vec3 normal;

out vec3 fragPos;
out vec3 fragNormal;

uniform mat4 transformationMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

void main() {
    vec4 worldPos = transformationMatrix * vec4(position, 1.0);
    fragPos = worldPos.xyz;

    mat3 normalMatrix = transpose(inverse(mat3(transformationMatrix)));
    fragNormal = normalize(normalMatrix * normal);

    gl_Position = projectionMatrix * viewMatrix * worldPos;
}
