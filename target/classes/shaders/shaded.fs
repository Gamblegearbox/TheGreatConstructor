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
        float diffuse = max(dot(vertexNormal, normalize(lightPosition)),0.0);

        color.rgb *= diffuse;
    }

    if(showDepth)
    {
        color.rgb *= 1.0 - depth;
    }

    fragColor = color;
}


