#version 330

in vec2 outTexCoord;
in vec3 mvVertexNormal;
in vec3 mvVertexPos;
in vec4 mlightviewVertexPos;
in mat4 outModelViewMatrix;

out vec4 fragColor;

void main()
{
    vec4 baseColour = vec4(1,1,1,1);

    fragColor = baseColour;

}