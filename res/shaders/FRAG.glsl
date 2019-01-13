#version 330

in vec3 gWorldPos;
in vec3 gNorm;
in vec2 gTexCoord;

out vec4 fragColor;

uniform sampler2D shading_sampler;
uniform sampler2D lightColor_sampler;

uniform sampler2D diffuseMap_sampler;
uniform sampler2D glossMap_sampler;

uniform vec3 lightPosition;
uniform float timeOfDay;


void main()
{

    vec3 position = vec3(0,0,0);//TODO: later: camera position
    vec3 lightDirection = lightPosition - position;
    lightDirection = normalize(lightDirection);


    float gradientCoordX = dot(gNorm, lightDirection ) * 0.5 + 0.5;
    float gradientCoordY = 1.0;

    vec4 shadingColor = texture(shading_sampler, vec2(gradientCoordX, gradientCoordY));
    vec4 lightColor = texture(lightColor_sampler, vec2(timeOfDay, 0));
    vec4 textureColor = texture(diffuseMap_sampler, gTexCoord);

    // CALC SPECULAR TERM TODO: just a test, make a clean implementation!
    float specular = 0;
    float SPECULAR_POWER = 128;
    float materialReflectance = 1.0 ;

    vec3 camera_direction = normalize(-gWorldPos);
    vec3 from_light_dir = -normalize(lightPosition);
    vec3 reflected_light = normalize(reflect(from_light_dir , gNorm));
    float specularFactor = max( dot(camera_direction, reflected_light), 0.0);
    specularFactor = pow(specularFactor, SPECULAR_POWER);

    materialReflectance = texture(glossMap_sampler, gTexCoord).a;
    specular = specularFactor * materialReflectance;
    shadingColor += vec4(specular, specular, specular, 1.0);

    vec4 finalColor = textureColor * shadingColor * lightColor;

    fragColor = clamp(finalColor,0,1);


}




