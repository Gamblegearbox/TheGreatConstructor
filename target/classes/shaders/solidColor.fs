#version 330

in vec2 outTexCoord;
in vec3 mvVertexNormal;
in vec3 mvVertexPos;
in vec4 mlightviewVertexPos;
in mat4 outModelViewMatrix;

out vec4 fragColor;

uniform vec3 color;

void main()
{
    vec4 baseColour = vec4(color,1);

    fragColor = baseColour;
}