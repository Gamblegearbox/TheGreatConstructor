#version 330

in vec3 _mvPosition;
in vec3 _mvNormal;
in vec2 _uvCoord;
in float _vDepth;

out vec4 fragColor;

uniform vec3 unicolorColor;
uniform vec3 lightPosition;

uniform bool isShaded;
uniform bool showDepth;
uniform bool enableNormalsToColor;

uniform bool hasDiffuseMap;
uniform bool hasNormalMap;
uniform bool hasGlossMap;
uniform bool hasIlluminationMap;

uniform sampler2D diffuseMap_sampler;
uniform sampler2D normalMap_sampler;
uniform sampler2D glossMap_sampler;
uniform sampler2D illuminationMap_sampler;

const float SPECULAR_POWER = 128;

void main()
{
    vec4 diffuseColor;
    vec3 normalColor;
    float specularIntensity;
    vec3 illuminationColor = vec3(0.5, 0.8, 0.2);
    float lightIntensity = 1.0;   //uniform
    float materialReflectance = 1.0 ; //uniform
    vec3 lightColor = vec3(1.0,1.0,1.0); //uniform

    float transparency = 1.0;   //TODO: put that in a material uniform thing
    vec4 finalColor;

    if(hasDiffuseMap) {
        finalColor = texture(diffuseMap_sampler, _uvCoord);
    }
    else {
        finalColor = vec4(unicolorColor, transparency);
    }

    //CALC NORMAL
    vec3 currentNormal;

    if(hasNormalMap) {
        currentNormal = normalize(texture(normalMap_sampler, _uvCoord).rgb * 2.0 - 1.0);
        currentNormal = normalize(_mvNormal + currentNormal);
    }
    else {
        currentNormal = _mvNormal;
    }

    if(enableNormalsToColor) {
            float r = (currentNormal.r + 1.0) * 0.5;
            float g = (currentNormal.g + 1.0) * 0.5;
            float b = (currentNormal.b + 1.0) * 0.5;

            finalColor.rgb = vec3(r,g,b);
    }
    else if(isShaded)
    {
        float finalShadeFactor = 0;
        // CALC AMBIENT
        float ambient = 0.05; //uniform

        // CALC DIFFUSE
        float diffuse = max(dot(currentNormal, normalize(lightPosition)),0.0);

        // CALC SPECULAR
        float specular = 0;

        //TODO: Variables for later


        vec3 camera_direction = normalize(-_mvPosition);
        vec3 from_light_dir = -normalize(lightPosition);
        vec3 reflected_light = normalize(reflect(from_light_dir , currentNormal));
        float specularFactor = max( dot(camera_direction, reflected_light), 0.0);
        specularFactor = pow(specularFactor, SPECULAR_POWER);

        if(hasGlossMap) {
            materialReflectance = texture(glossMap_sampler, _uvCoord).r;
        }

        specular = specularFactor * lightIntensity * materialReflectance;

        //ADD THEM TOGETHER
        finalShadeFactor = ambient + diffuse + specular;

        finalColor.rgb *= finalShadeFactor;

        if(hasIlluminationMap)
        {
            if(finalShadeFactor < 0.1)
            {
                finalColor.rgb += texture(illuminationMap_sampler, _uvCoord).rgb * illuminationColor;
            }
        }
    }

    if(showDepth) {
        finalColor.rgb *= 1.0 - _vDepth;
    }

    fragColor = clamp(finalColor,0,1);
}




