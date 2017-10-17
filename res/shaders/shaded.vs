#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec3 normal;
layout (location=2) in vec2 uvCoord;

out vec3 vertexNormal;
out vec2 vertexUvCoord;
out float depth;

uniform mat4 viewProjectionMatrix;
uniform mat4 modelMatrix;

void main()
{
    mat4 modelViewProjectionMatrix = viewProjectionMatrix * modelMatrix;
    mat4 normalMatrix = transpose(modelViewProjectionMatrix);

    gl_Position = modelViewProjectionMatrix * vec4(position, 1.0);
    vertexNormal = vec3(normalMatrix * vec4(normal, 0));
    vertexUvCoord = uvCoord;
    depth = gl_Position.z / 7.0;       //TODO: put value for wireframe depth in uniform
}