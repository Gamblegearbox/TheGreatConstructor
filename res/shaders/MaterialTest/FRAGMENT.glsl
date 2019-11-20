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
uniform sampler2D glossMap_sampler;
uniform sampler2D illuminationMap_sampler;
uniform sampler2D reflectionMap_sampler;
uniform sampler2D lightColorMap_sampler;
uniform vec2 windowSize;

uniform vec3 lightPosition;
uniform float timeOfDay;
uniform vec3 cameraPosition;


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
    int SPECULAR_POWER = 64;
    vec4 shadow = texture(reflectionMap_sampler, vec2(gl_FragCoord.x, 1-gl_FragCoord.y) / 1024);
    shadow.rgb *= 0.1;
    vec4 colorAmbient = vec4(0.0,0.0,0.0,1.0);
    vec4 colorDiffuse = texture(diffuseMap_sampler, gTexCoord);
    vec4 colorSpecular = vec4(0.0, 0.0, 0.0, 1.0);

    //if(!(texture(illuminationMap_sampler, gTexCoord).a > 0.1)){

        //DIFFUSE
        vec3 toLightSource = normalize(lightPosition);
        float diffuseFactor = max(dot(gNormal_WorldSpace, toLightSource), 0.0);

        //colorDiffuse.rgb *= diffuseFactor;

        //SPECULAR
        colorSpecular = vec4(1.0, 1.0, 1.0, 1.0);
        vec3 camDirection = normalize(cameraPosition - gPos_WorldSpace);
        vec3 fromLightDir = -toLightSource;
        vec3 reflectedLight = normalize(reflect(fromLightDir, gNormal_WorldSpace));
        float specularFactor = max(dot(camDirection, reflectedLight), 0.0);
        specularFactor = pow(specularFactor, SPECULAR_POWER);

        colorSpecular.rgb *= specularFactor * texture(glossMap_sampler, gTexCoord).a;

    //}

    vec4 phong = clamp((colorAmbient + colorSpecular + colorDiffuse),0.0,1.0);

    //DIFFUSE
    vec4 tex_Diffuse = texture(diffuseMap_sampler, gTexCoord);

    //ILLUMINATION
    vec4 tex_Illum = texture(illuminationMap_sampler, gTexCoord);

    //NORMALS
    // vec3 textureNormal = normalize(texture(normalMap_sampler, gTexCoord).rgb * 2.0 - 1.0);
    vec3 normal = normalize(gNormal_WorldSpace.rgb);

    //REFLECTION
    int power = 4;
    float front = 0.0;
    float side = 1.0;

    vec3 objToCam = normalize(cameraPosition - gPos_WorldSpace);
    float facing = max(dot(objToCam, normal), 0.0);
    facing = pow(facing, power);

    vec3 smpDir = reflect(normalize(objToCam), normal);
    vec2 reflCoords = getReflectionUV(smpDir);
    vec4 tex_Reflect = texture(reflectionMap_sampler, reflCoords);

    vec4 tex_Gloss = texture(glossMap_sampler, gTexCoord);
    float reflectionMask = mix(side, front, facing) * tex_Gloss.r;

    //SPLIT SCREEN SETTINGS
    int splitScreenBorder_1 = 300;
    int splitScreenBorder_2 = 600;
    int splitScreenBorder_3 = 900;
    int splitLineWidth = 4;

    vec4 splitLineColor = vec4(0.1,0.1,0.1,1.0);

    //SPLIT SCREEN CONTENT I
    if(gl_FragCoord.x < splitScreenBorder_1) {
        fragColor = tex_Diffuse;

        if(gl_FragCoord.x > splitScreenBorder_1 - splitLineWidth && gl_FragCoord.x < splitScreenBorder_1){
            fragColor = splitLineColor;
        }
    }

    //SPLIT SCREEN CONTENT II
    else if (gl_FragCoord.x < splitScreenBorder_2) {
        float facing = mix(front, side, facing);
        fragColor = vec4(facing, facing, facing, 1.0);

        if(gl_FragCoord.x > splitScreenBorder_2 - splitLineWidth && gl_FragCoord.x < splitScreenBorder_2) {
            fragColor = splitLineColor;
        }
    }


    //SPLIT SCREEN CONTENT III
    else if (gl_FragCoord.x < splitScreenBorder_3) {
        fragColor = clamp(mix(shadow,colorDiffuse + colorSpecular,diffuseFactor), 0.0, 1.0);//phong;

        if(gl_FragCoord.x > splitScreenBorder_3 - splitLineWidth && gl_FragCoord.x < splitScreenBorder_3) {
            fragColor = splitLineColor;
        }
    }

    //SPLIT SCREEN CONTENT IV
    else {
        fragColor = clamp(mix(tex_Diffuse, tex_Reflect, reflectionMask),0.0,1.0);
    }

}




