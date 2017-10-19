#version 330

in vec3 vertexNormal;
in vec2 vertexUvCoord;
in float depth;
in mat4 outModelViewMatrix;

out vec4 fragColor;

uniform vec3 unicolorColor;
uniform vec3 lightPosition;
uniform bool isShaded;
uniform bool showDepth;
uniform bool isTextured;
uniform sampler2D texture_sampler;


void main()
{
    float transparency = 1.0;   //TODO: put that in a material uniform thing
    vec4 color;

    if(isTextured)
    {
        color = texture(texture_sampler, vertexUvCoord);
    }
    else
    {
        color = vec4(unicolorColor, transparency);
    }

    if(isShaded)
    {
        float intensity = max(dot(vertexNormal, normalize(lightPosition)),0.0);
        float diffuse = 1;
        /*
        if (intensity > 0.95)
        		diffuse = 0.75;
        	else if (intensity > 0.5)
        		diffuse = 0.5;
        	else if (intensity > 0.25)
        		diffuse = 0.25;
        	else
        		diffuse = 0.1;
        */
        color.rgb *= intensity;
    }

    if(showDepth)
    {
        color.rgb *= 1.0 - depth;
    }

    fragColor = color;
}


