#version 330

in vec3 gWorldSpacePosition;
in vec3 gWorldSpaceNormal;
in vec2 gTexCoord;

out vec4 fragColor;

uniform sampler2D diffuseMap_sampler;
uniform sampler2D normalMap_sampler;
uniform sampler2D glossMap_sampler;
uniform sampler2D illuminationMap_sampler;

uniform sampler2D lightColor_sampler;
uniform vec3 lightPosition;
uniform float timeOfDay;
uniform vec3 cameraPosition;

void main()
{
    int SPECULAR_POWER = 64;

    vec4 colorAmbient = vec4(0.15,0.15,0.15,1.0);
    vec4 colorDiffuse = texture(diffuseMap_sampler, gTexCoord);
    vec4 colorLight = texture(lightColor_sampler, vec2(timeOfDay, 0.0));
    vec4 colorSpecular = vec4(0.0, 0.0, 0.0, 1.0);

    if(!(texture(illuminationMap_sampler, gTexCoord).a > 0.1)){

        //DIFFUSE
        vec3 toLightSource = normalize(lightPosition);
        float diffuseFactor = max(dot(gWorldSpaceNormal, toLightSource), 0.0);

        colorDiffuse.rgb *= diffuseFactor;
        colorDiffuse.rgb *= colorLight.rgb;

        //SPECULAR
        colorSpecular = vec4(1.0, 1.0, 1.0, 1.0);
        vec3 camDirection = normalize(cameraPosition - gWorldSpacePosition);
        vec3 fromLightDir = -toLightSource;
        vec3 reflectedLight = normalize(reflect(fromLightDir, gWorldSpaceNormal));
        float specularFactor = max(dot(camDirection, reflectedLight), 0.0);
        specularFactor = pow(specularFactor, SPECULAR_POWER);

        colorSpecular.rgb *= specularFactor * texture(glossMap_sampler, gTexCoord).a;
        colorSpecular.rgb *= colorLight.rgb;
    }

    fragColor = clamp((colorAmbient + colorSpecular + colorDiffuse),0.0,1.0);
}




