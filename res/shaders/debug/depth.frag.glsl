#version 330

in vec3 _mvPosition;
in vec3 _mvNormal;
in vec2 _uvCoord;
in float _vDepth;

out vec4 fragColor;


void main()
{
    float depthRange = 25.0;
    vec4 finalColor = vec4(1,1,1,1);
    finalColor.rgb *= 1.0 - (_vDepth / depthRange);

    fragColor = clamp(finalColor,0,1);
}




