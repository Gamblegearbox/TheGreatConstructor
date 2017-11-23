#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec3 normal;
layout (location=2) in vec2 uvCoord;

out vec3 _mvPosition;
out vec3 _mvNormal;
out vec2 _uvCoord;
out float _vDepth;

uniform mat4 modelViewMatrix;
uniform mat4 projectionMatrix;

void main()
{
    vec4 mvPos = modelViewMatrix * vec4(position, 1.0);
    gl_Position = projectionMatrix * mvPos;
    _uvCoord = uvCoord;

    _mvNormal = normalize(modelViewMatrix * vec4(normal, 0.0)).xyz;
    _mvPosition = mvPos.xyz;
    _vDepth = gl_Position.z / 7.0;       //TODO: put value for wireframe depth in uniform
}