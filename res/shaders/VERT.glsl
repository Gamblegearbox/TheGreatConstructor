#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec3 normal;
layout (location=2) in vec2 uvCoord;

out vec3 vModelSpacePosition;
out vec3 vModelSpaceNormal;
out vec2 vTexCoord;

uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;
uniform mat4 modelMatrix;

void main()
{
    vec4 vertexModelSpacePos = modelMatrix * vec4(position, 1.0);
    vec4 vertexViewSpacePos = viewMatrix * vertexModelSpacePos;

    vec4 normalModelSpacePos = modelMatrix * vec4(normal, 1.0);
    vec4 normalViewSpacePos = viewMatrix * normalModelSpacePos;

    //OUT
    vModelSpaceNormal = vec3(modelMatrix * vec4(normal, 0.0));
    vModelSpacePosition = vertexModelSpacePos.xyz;
    vTexCoord = uvCoord;

    //GL OUT
    gl_Position = projectionMatrix * vertexViewSpacePos;
}
