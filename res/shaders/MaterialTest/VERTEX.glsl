#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec3 normal;
layout (location=2) in vec2 uvCoord;

out vec3 vPos_ObjectSpace;
out vec3 vPos_WorldSpace;
out vec3 vPos_ViewSpace;

out vec3 vNormal_ObjectSpace;
out vec3 vNormal_WorldSpace;
out vec3 vNormal_ViewSpace;

out vec2 vTexCoord;

uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;
uniform mat4 modelMatrix;

void main()
{
    vec4 pos_WorldSpace = modelMatrix * vec4(position, 1.0);
    vec4 pos_ViewSpace = viewMatrix * pos_WorldSpace;

    vec4 normal_WorldSpace = modelMatrix * vec4(normal, 0.0);
    vec4 normal_ViewSpace = viewMatrix * normal_WorldSpace;

    //OUT
    vPos_ObjectSpace = position;
    vPos_WorldSpace = pos_WorldSpace.xyz;
    vPos_ViewSpace = pos_ViewSpace.xyz;

    vNormal_ObjectSpace = normal;
    vNormal_WorldSpace = normal_WorldSpace.xyz;
    vNormal_ViewSpace = normal_ViewSpace.xyz;

    vTexCoord = uvCoord;

    //GL OUT
    gl_Position = projectionMatrix * pos_ViewSpace;
}
