#version 330

in vec3 _mvPosition;
in vec3 _mvNormal;
in vec2 _uvCoord;
in float _vDepth;

out vec4 fragColor;

uniform sampler2D shading_sampler;
uniform sampler2D lightColor_sampler;
uniform vec4 fogColor;
uniform vec3 lightPosition;
uniform float daytime;


void main()
{
    //GET GRADIENT COLOR
    vec3 position = vec3(0,0,0);//TODO: later: camera position
    vec3 lightDirection = lightPosition - position;
    lightDirection = normalize(lightDirection);

    //GRADIENT TEST
    float gradientCoordX = dot(_mvNormal, lightDirection ) * 0.5 + 0.5;
    float gradientCoordY = 1.0;

    vec4 shadingColor = texture(shading_sampler, vec2(gradientCoordX, gradientCoordY));
    vec4 lightColor = texture(lightColor_sampler, vec2(daytime, 0));


    vec4 finalColor = lightColor + shadingColor * 0.5;

    fragColor = clamp(finalColor,0,1);


}




