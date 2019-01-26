#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec3 normal;
layout (location=2) in vec2 uvCoord;

out vec3 vPosition;
out vec3 vNormal;
out vec2 vTexCoord;


uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;
uniform mat4 modelMatrix;

void main()
{
    vec4 modelSpacePos = modelMatrix * vec4(position, 1.0);
    vec4 viewSpacePos = viewMatrix * modelSpacePos;

    gl_Position = projectionMatrix * viewSpacePos;
    vTexCoord = uvCoord;

    mat4 modelViewMatrix = viewMatrix * modelMatrix;
    vNormal = (modelViewMatrix * vec4(normal, 0.0)).xyz;
    vPosition = viewSpacePos.xyz;
}