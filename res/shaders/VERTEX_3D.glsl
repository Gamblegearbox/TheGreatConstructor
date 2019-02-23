#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec3 normal;
layout (location=2) in vec2 uvCoord;

out vec3 vWorldSpacePosition;
out vec3 vWorldSpaceNormal;
out vec2 vTexCoord;

uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;
uniform mat4 modelMatrix;

void main()
{
    vec4 vertexWorldSpacePos = modelMatrix * vec4(position, 1.0);
    vec4 vertexViewSpacePos = viewMatrix * vertexWorldSpacePos;

    vec4 normalWorldSpacePos = modelMatrix * vec4(normal, 1.0);
    vec4 normalViewSpacePos = viewMatrix * normalWorldSpacePos;

    //OUT
    vWorldSpaceNormal = vec3(modelMatrix * vec4(normal, 0.0));
    vWorldSpacePosition = vertexWorldSpacePos.xyz;
    vTexCoord = uvCoord;

    //GL OUT
    gl_Position = projectionMatrix * vertexViewSpacePos;
}
