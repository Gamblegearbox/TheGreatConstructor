#version 330

in vec3 _mvPosition;
in vec3 _mvNormal;
in vec2 _uvCoord;
in float _vDepth;

out vec4 fragColor;

uniform vec3 lightPosition;

uniform bool hasDiffuseMap;
uniform bool hasNormalMap;
uniform bool hasGlossMap;
uniform bool hasIlluminationMap;

uniform sampler2D diffuseMap_sampler;
uniform sampler2D normalMap_sampler;
uniform sampler2D glossMap_sampler;
uniform sampler2D illuminationMap_sampler;

uniform vec4 fogColor;

const float SPECULAR_POWER = 256;

void main()
{
    vec4 backupColor = vec4(1,1,1,1);

    //UNIFORMS?
    vec4 illuminationColor = vec4(0.5, 0.8, 0.2, 1.0);
    vec3 lightColor = vec3(1.0,1.0,1.0);
    float lightIntensity = 1.0;
    float materialReflectance = 1.0 ;


    //SET COLOR
    vec4 color = backupColor;

    if(hasDiffuseMap) {
        color = texture(diffuseMap_sampler, _uvCoord);
    }

    //CALC NORMAL
    vec3 normal = _mvNormal;

    if(hasNormalMap) {
        normal = normalize(texture(normalMap_sampler, _uvCoord).rgb * 2.0 - 1.0);
        normal = normalize(_mvNormal + normal);
    }


    // CALC AMBIENT TERM
    float ambient = 0.25; //TODO:uniform

    // CALC DIFFUSE TERM
    vec3 position = vec3(0,0,0);//TODO: later: camera position
    vec3 lightDirection = lightPosition - position;
    lightDirection = normalize(lightDirection);

    float diffuse = max(0.0, dot(lightDirection, normal));

    // CALC SPECULAR TERM
    float specular = 0;

    //TODO: Variables for later
    vec3 camera_direction = normalize(-_mvPosition);
    vec3 from_light_dir = -normalize(lightPosition);
    vec3 reflected_light = normalize(reflect(from_light_dir , normal));
    float specularFactor = max( dot(camera_direction, reflected_light), 0.0);
    specularFactor = pow(specularFactor, SPECULAR_POWER);

    if(hasGlossMap) {
        materialReflectance = texture(glossMap_sampler, _uvCoord).r;
    }

    specular = specularFactor * lightIntensity * materialReflectance;

    float finalShadeFactor = ambient + diffuse + specular;

    color.rgb *= finalShadeFactor;

    /*
    if(hasIlluminationMap)
    {
        //if(finalShadeFactor < 0.1) //TODO: make 0.1 a treshold variable and pass it as uniform
        //{
            color.rgba += texture(illuminationMap_sampler, _uvCoord).rgba;// * illuminationColor; //TODO: think about that ...color from texture or fix color.. or both
        //}
    }
    */


    //FOG https://vicrucann.github.io/tutorials/osg-shader-fog/
    const float FOG_MIN = 5;
    const float FOG_MAX = 50;
    float fogFactor;

    fogFactor = 1.0 - (FOG_MAX - _vDepth) / (FOG_MAX - FOG_MIN);

    if(_vDepth >= FOG_MAX){
        fogFactor = 1;
    }

    if(_vDepth <= FOG_MIN){
        fogFactor = 0;
    }

    color = clamp(color,0,1);
    fragColor = mix(color, fogColor, fogFactor);
}




