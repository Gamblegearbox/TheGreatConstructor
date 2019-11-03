#version 330

in vec3 gWorldSpacePosition;
in vec3 gWorldSpaceNormal;
in vec2 gTexCoord;

out vec4 fragColor;


void main()
{
    float depthRange = 100.0;

    vec4 tintColor = vec4(1.0,1.0,1.0,1.0);

    //http://in2gpu.com/2014/07/22/create-fog-shader/
    float distance = gl_FragCoord.z / gl_FragCoord.w;

    tintColor.rgb *= distance / depthRange;

    fragColor = clamp(tintColor,0.0,1.0);
}




