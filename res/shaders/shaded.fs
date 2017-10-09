#version 330

in vec3 vertexNormal;
in vec3 vertexColor;
in float depth;

out vec4 fragColor;

uniform int renderMode;
uniform vec3 unicolorColor;
uniform vec3 wireframeColor;
uniform vec3 lightPosition;
uniform bool isShaded;
uniform bool showDepth;

void main()
{
    float transparency = 1.0;   //TODO: put that in a material uniform thing
    vec3 color;

    switch(renderMode)
    {
        //TEXTURED
        case 0:
            color = vertexColor;
            break;

        //UNICOLOR
        case 1:
            color = unicolorColor;
            break;

        //WIREFRAME
        case 2:
            color = wireframeColor;
            break;

        default :
            color = vec3(1.0, 1.0, 0.0);    //just a fall back color
            break;
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


