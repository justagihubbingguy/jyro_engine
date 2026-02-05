#version 330 core

in vec2 pass_textureCoords;
in vec3 fragPos;
in mat3 TBN;

out vec4 out_Color;

uniform sampler2D textureSampler;
uniform sampler2D normalMapSampler;
uniform sampler2D metallicRoughnessSampler;

uniform float metallic;
uniform float roughness;

uniform vec3 lightPosition;
uniform vec3 lightColor;
uniform vec3 cameraPosition;

void main(void) {
    vec3 sampledNormal = texture(normalMapSampler, pass_textureCoords).rgb;
    sampledNormal = normalize(sampledNormal * 2.0 - 1.0);
    vec3 N = normalize(TBN * sampledNormal);

    vec4 orm = texture(metallicRoughnessSampler, pass_textureCoords);
    float finalRoughness = orm.g * roughness;
    float finalMetallic = orm.b * metallic;

    vec3 L = normalize(lightPosition - fragPos);
    vec3 V = normalize(cameraPosition - fragPos);
    vec3 H = normalize(L + V);

    vec3 albedo = texture(textureSampler, pass_textureCoords).rgb;

    vec3 F0 = mix(vec3(0.04), albedo, finalMetallic);

    vec3 diffuse = (1.0 - finalMetallic) * albedo / 3.14159;

    float NdotH = max(dot(N, H), 0.0);
    float NdotL = max(dot(N, L), 0.0);

    float alpha = max(finalRoughness * finalRoughness, 0.001);
    vec3 specular = F0 * pow(NdotH, 1.0 / alpha);

    vec3 color = (diffuse + specular) * lightColor * NdotL;

    // color = pow(color, vec3(1.0/2.2));

    out_Color = vec4(color, 1.0);
}
