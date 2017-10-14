#version 330

in vec3 vertexNormal;
in vec3 vertexColor;
in float depth;

out vec4 fragColor;

uniform vec3 unicolorColor;
uniform vec3 lightPosition;
uniform bool isShaded;
uniform bool showDepth;
uniform bool isTextured;

void main()
{
    float transparency = 1.0;   //TODO: put that in a material uniform thing
    vec3 color;

    if(isTextured)
    {
        color = vertexColor;
    }
    else
    {
        color = unicolorColor;
    }

    if(isShaded)
    {
        float intensity;
        float diffuse;
        intensity = dot(normalize(lightPosition), normalize(vertexNormal));

        if (intensity > 0.95)
            diffuse = 1.0;
        else if (intensity > 0.5)
            diffuse =  0.75;
        else if (intensity > 0.25)
            diffuse = 0.5;
        else
            diffuse = 0.25;

        color *= diffuse;
    }

    if(showDepth)
    {
        color *= 1.0 - depth;
    }

    fragColor = vec4(color, transparency);
}


