#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec3 normal;
layout (location=2) in vec2 uvCoord;

out vec2 vTexCoord;

uniform mat4 projectionMatrix;
uniform mat4 modelMatrix;

void main()
{
    vec4 vertexModelSpacePos = modelMatrix * vec4(position, 1.0);

    gl_Position = projectionMatrix * vertexModelSpacePos;
    vTexCoord = uvCoord;
}