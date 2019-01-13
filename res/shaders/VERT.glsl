#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec3 normal;
layout (location=2) in vec2 uvCoord;

out vec3 vWorldPos;
out vec3 vNorm;
out vec2 vTexCoord;

uniform mat4 modelViewMatrix;
uniform mat4 projectionMatrix;

void main()
{
    vec4 mvPos = modelViewMatrix * vec4(position, 1.0);
    gl_Position = projectionMatrix * mvPos;
    vTexCoord = uvCoord;

    vNorm = normalize(modelViewMatrix * vec4(normal, 0.0)).xyz;
    vWorldPos = mvPos.xyz;
}