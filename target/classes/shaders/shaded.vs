#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec3 normal;
layout (location=2) in vec2 uvCoord;

out vec3 vertexNormal;
out vec2 vertexUvCoord;
out float depth;
out mat4 outModelViewMatrix;

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;

void main()
{
    vec4 pos = modelViewMatrix * vec4(position, 1.0);
    gl_Position = projectionMatrix * pos;

    vertexNormal = normalize(modelViewMatrix * vec4(normal, 0.0)).xyz;
    vertexUvCoord = uvCoord;
    depth = gl_Position.z / 7.0;       //TODO: put value for wireframe depth in uniform

    outModelViewMatrix = modelViewMatrix;
}