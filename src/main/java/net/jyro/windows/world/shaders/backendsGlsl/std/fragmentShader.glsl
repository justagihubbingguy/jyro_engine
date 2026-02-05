#version 330 core
in vec2 pass_textureCoords;
in vec3 fragPos;
in mat3 TBN;

out vec4 out_Color;

uniform sampler2D textureSampler;
uniform sampler2D normalMapSampler;
uniform vec3 lightPosition;
uniform vec3 lightColor;

void main(void) {
    vec3 sampledNormal = texture(normalMapSampler, pass_textureCoords).rgb;
    sampledNormal = normalize(sampledNormal * 2.0 - 1.0);
    vec3 normal = normalize(TBN * sampledNormal);

    vec3 lightDir = normalize(lightPosition - fragPos);
    float diff = max(dot(normal, lightDir), 0.1);
    vec3 diffuse = diff * lightColor;

    vec4 texColor = texture(textureSampler, pass_textureCoords);
    //out_Color = vec4(diffuse, 1.0) * texColor;
    out_Color = bec3
}
