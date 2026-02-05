#version 330 core
in vec3 position;
in vec2 textureCoords;
in vec3 normal;
in vec3 tangent;

out vec2 pass_textureCoords;
out vec3 fragPos;
out mat3 TBN;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main(void) {
    vec4 worldPos = transformationMatrix * vec4(position, 1.0);
    gl_Position = projectionMatrix * viewMatrix * worldPos;
    pass_textureCoords = textureCoords;
    fragPos = worldPos.xyz;

    vec3 N = normalize(mat3(transformationMatrix) * normal);
    vec3 T = normalize(mat3(transformationMatrix) * tangent);
    vec3 B = cross(N, T);
    TBN = mat3(T, B, N);
}
