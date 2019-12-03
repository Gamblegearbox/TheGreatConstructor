#version 330

in vec3 gPos_ObjectSpace;
in vec3 gPos_WorldSpace;
in vec3 gPos_ViewSpace;

in vec3 gNormal_ObjectSpace;
in vec3 gNormal_WorldSpace;
in vec3 gNormal_ViewSpace;

in vec2 gTexCoord;

in vec4 gl_FragCoord;

out vec4 fragColor;

uniform sampler2D diffuseMap_sampler;
uniform sampler2D normalMap_sampler;
uniform sampler2D materialInfo_sampler;
uniform sampler2D shadowPattern_sampler;
uniform sampler2D reflectionMap_sampler;
uniform sampler2D lightColorMap_sampler;
uniform vec2 windowSize;

uniform vec3 lightPosition;
uniform float timeOfDay;
uniform vec3 cameraPosition;
uniform float illumination;

vec2 getReflectionUV(vec3 _smpDir){
    vec2 smpUV;

    smpUV.x = atan(_smpDir.x, _smpDir.z) / 3.14159265359;
    smpUV.y = 1.0 - (acos(_smpDir.y) / 1.57079632679);

    smpUV = (smpUV * 0.5) + vec2(0.5, 0.5);

    return smpUV;
}

vec4 createNormalsColor(vec3 _normal){
    vec3 normalColor = normalize(_normal);
    normalColor = (normalColor + 1) * 0.5;

    return vec4(normalColor, 1.0);
}

void main()
{
    const int SPECULAR_POWER = 32;
    const int FAKE_FRESNEL_POWER = 2;
    const int SHADOW_PATTERN_SIZE = 42;

    vec4 colorLight = texture(lightColorMap_sampler, vec2(timeOfDay,0.1));
    vec4 colorDiffuse = texture(diffuseMap_sampler, gTexCoord);
    vec4 colorSpecular;
    float ambientBrightness = 0.1; //TODO: uniform
    float reflOverride = 0.0; //TODO: uniform

    float diffuseFactor;
    vec4 shadowPattern;

    if(texture(materialInfo_sampler, gTexCoord).g > 0.1 && illumination != 0.0){
        colorSpecular = vec4(0.0,0.0,0.0,1.0);
        diffuseFactor = 1.0;
    }
    else {
        //DIFFUSE
        vec3 toLightSource = normalize(lightPosition);
        diffuseFactor = max(dot(gNormal_WorldSpace, toLightSource), 0.0);
        colorDiffuse *= colorLight;
        shadowPattern = texture(shadowPattern_sampler, vec2(gl_FragCoord.x, gl_FragCoord.y) / SHADOW_PATTERN_SIZE);
        shadowPattern.rgb *= colorDiffuse.rgb * diffuseFactor;

        //SPECULAR
        vec3 camDirection = normalize(cameraPosition - gPos_WorldSpace);
        vec3 fromLightDir = -toLightSource;
        vec3 reflectedLight = normalize(reflect(fromLightDir, gNormal_WorldSpace));
        float specularFactor = max(dot(camDirection, reflectedLight), 0.0);
        specularFactor = pow(specularFactor, SPECULAR_POWER);
        colorSpecular.rgb = colorLight.rgb * specularFactor * texture(materialInfo_sampler, gTexCoord).r;
    }

    //REFLECTION
    float facing = 0;
    float front = 0.0;
    float side = 1.0;

    vec3 objToCam = normalize(cameraPosition - gPos_WorldSpace);
    facing = max(dot(objToCam, gNormal_WorldSpace), 0.0);
    facing = pow(facing, FAKE_FRESNEL_POWER);

    vec3 smpDir = reflect(normalize(objToCam), gNormal_WorldSpace);
    vec2 reflCoords = getReflectionUV(smpDir);
    vec4 tex_Reflect = texture(reflectionMap_sampler, reflCoords * 2.0);
    float reflectionMask = clamp(mix(side, front, facing) * texture(materialInfo_sampler, gTexCoord).r + reflOverride, 0.0,1.0);

    //SPLIT SCREEN SETTINGS
    const int splitScreenBorder_1 = 150;
    const int splitScreenBorder_2 = 300;
    const int splitScreenBorder_3 = 450;
    const int splitLineWidth = 4;
    const vec4 splitLineColor = vec4(0.0,0.0,0.0,1.0);

    //SPLIT SCREEN CONTENT I
    if(gl_FragCoord.x < splitScreenBorder_1) {
        fragColor = colorDiffuse;

        if(gl_FragCoord.x > splitScreenBorder_1 - splitLineWidth && gl_FragCoord.x < splitScreenBorder_1){
            fragColor = splitLineColor;
        }
    }

    //SPLIT SCREEN CONTENT II
    else if (gl_FragCoord.x < splitScreenBorder_2) {
        vec4 phong = clamp((ambientBrightness + colorSpecular + vec4(0.8,0.8,0.8,1.0)),0.0,1.0);
        fragColor = clamp(mix(shadowPattern + ambientBrightness, phong, diffuseFactor), 0.0, 1.0);

        if(gl_FragCoord.x > splitScreenBorder_2 - splitLineWidth && gl_FragCoord.x < splitScreenBorder_2) {
            fragColor = splitLineColor;
        }
    }

    //SPLIT SCREEN CONTENT III
    else if (gl_FragCoord.x < splitScreenBorder_3) {

        fragColor = vec4(reflectionMask, reflectionMask, reflectionMask, 1.0);

        if(gl_FragCoord.x > splitScreenBorder_3 - splitLineWidth && gl_FragCoord.x < splitScreenBorder_3) {
            fragColor = splitLineColor;
        }
    }

    //SPLIT SCREEN CONTENT IV
    else {
        vec4 shaded = clamp(mix(shadowPattern + ambientBrightness * colorDiffuse, colorDiffuse + colorSpecular + ambientBrightness, diffuseFactor), 0.0, 1.0);
        //vec4 shaded = clamp(colorAmbient + colorDiffuse + colorSpecular, 0.0, 1.0);
        //shaded.rgb *= diffuseFactor;
        vec4 reflection = tex_Reflect + colorSpecular;
        fragColor = clamp(mix(shaded, reflection, reflectionMask),0.0,1.0);
    }

}




