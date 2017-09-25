#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec3 normal;
layout (location=2) in vec3 color;

out vec3 vertexNormal;
out vec3 vertexColor;

uniform mat4 projectionMatrix;
uniform mat4 modelMatrix;

void main()
{
    mat4 modelViewMatrix = projectionMatrix * modelMatrix;
    mat4 normalMatrix = transpose(modelViewMatrix);

    gl_Position = modelViewMatrix * vec4(position, 1.0);
    vertexNormal = vec3(normalMatrix * vec4(normal, 0));
    vertexColor = color;
}