#version 330

in vec3 vertexNormal;
in vec3 vertexColor;

out vec4 fragColor;

uniform int renderMode;
uniform vec3 unicolorColor;
uniform vec3 wireframeColor;
uniform vec3 lightPosition;

void main()
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


    switch(renderMode)
    {
        //SHADED
        case 0:

            fragColor = vec4(vertexColor * diffuse, 1.0) ;
            break;

        //SHADED_UNICOLOR
        case 1:

            fragColor = vec4(unicolorColor * diffuse, 1.0);
            break;

        //WIREFRAME
        case 2:

            fragColor = vec4(wireframeColor, 1.0);
            break;

        default :
            fragColor = vec4(1.0, 1.0, 0.0, 1.0);
    }
}


