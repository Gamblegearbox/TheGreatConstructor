#version 330

in vec3 gPosition;
in vec3 gNormal;
in vec2 gTexCoord;

out vec4 fragColor;

uniform sampler2D shading_sampler;
uniform sampler2D lightColor_sampler;

uniform sampler2D diffuseMap_sampler;
uniform sampler2D glossMap_sampler;
uniform sampler2D illuminationMap_sampler;

uniform vec3 lightPosition;
uniform float timeOfDay;


void main()
{
    //DIFFUSE COMPONENT
    //float gradientCoordX = (dot(gNorm, lightDirection ) * 0.5) + 0.5;
    //float gradientCoordY = 1.0;
    vec4 diffuseColor;// = texture(shading_sampler, vec2(gradientCoordX, gradientCoordY));
    vec4 lightColor = texture(lightColor_sampler, vec2(timeOfDay, 0));
    vec4 textureColor = texture(diffuseMap_sampler, gTexCoord);
    float diffuseFactor = max(dot(gNormal, lightPosition), 0.0);

    diffuseColor = vec4(diffuseFactor,diffuseFactor,diffuseFactor,1);

    //SPECULAR TERM
    float specular = 0;
    float SPECULAR_POWER = 128;
    float materialReflectance = 1.0 ;

    vec3 camera_direction = normalize(-gPosition);
    vec3 from_light_dir = normalize(-lightPosition);
    vec3 reflected_light = normalize(reflect(from_light_dir , gNormal));
    float specularFactor = max( dot(camera_direction, reflected_light), 0.0);
    specularFactor = pow(specularFactor, SPECULAR_POWER);

    materialReflectance = texture(glossMap_sampler, gTexCoord).a;
    specular = specularFactor * materialReflectance;
    diffuseColor += vec4(specular, specular, specular, 1.0);


    vec4 finalColor;
    if(texture(illuminationMap_sampler, gTexCoord).a > 0.0){
        finalColor = textureColor;
    } else {
        finalColor = textureColor * diffuseColor * lightColor;
    }

    fragColor = clamp(finalColor,0,1);


}




