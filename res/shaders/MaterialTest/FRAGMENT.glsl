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
    //DIFFUSE
    vec4 tex_Diffuse = texture(diffuseMap_sampler, gTexCoord);

    //ILLUMINATION TODO:
    vec4 tex_Illum = texture(illuminationMap_sampler, gTexCoord);

    //NORMALS
    vec3 textureNormal = normalize(texture(normalMap_sampler, gTexCoord).rgb * 2.0 - 1.0);
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
    int splitScreenFrationWidth_1 = 150;
    int splitScreenFrationWidth_2 = 150;
    int splitScreenFrationWidth_3 = 150;
    int splitScreenFrationWidth_4 = 150;
    int splitLineHalfWidth = 2;

    float splitOffset = windowSize.x / splitScreenFrationWidth_1 * 5;
    vec4 splitLineColor = vec4(0.1,0.1,0.1,1.0);

    //SPLIT SCREEN CONTENT
    if(gl_FragCoord.x < splitScreenFrationWidth_1) {
        fragColor = tex_Diffuse;
    } else if (gl_FragCoord.x < splitScreenFrationWidth_1 + splitScreenFrationWidth_2) {
        fragColor = tex_Reflect;
    } else if (gl_FragCoord.x < splitScreenFrationWidth_1 + splitScreenFrationWidth_2 + splitScreenFrationWidth_3) {
        float reflectionMask = mix(side, front, facing);
        fragColor = vec4(reflectionMask, reflectionMask, reflectionMask, 1.0);
    } else if (gl_FragCoord.x < splitScreenFrationWidth_1 + splitScreenFrationWidth_2 + splitScreenFrationWidth_3 + splitScreenFrationWidth_4) {
        fragColor = vec4(reflectionMask, reflectionMask, reflectionMask, 1.0);
    } else {
        fragColor = clamp(mix(tex_Diffuse, tex_Reflect, reflectionMask),0.0,1.0);
    }

    //SPLIT SCREEN LINES
    if(gl_FragCoord.x > splitScreenFrationWidth_1 - splitLineHalfWidth && gl_FragCoord.x < splitScreenFrationWidth_1 + splitLineHalfWidth){
        fragColor = splitLineColor;
    }

    if(gl_FragCoord.x > splitScreenFrationWidth_1 + splitScreenFrationWidth_2 - splitLineHalfWidth && gl_FragCoord.x < splitScreenFrationWidth_1 + splitScreenFrationWidth_2 + splitLineHalfWidth) {
        fragColor = splitLineColor;
    }

    if(gl_FragCoord.x > splitScreenFrationWidth_1 + splitScreenFrationWidth_2 + splitScreenFrationWidth_3 - splitLineHalfWidth && gl_FragCoord.x < splitScreenFrationWidth_1 + splitScreenFrationWidth_2 + splitScreenFrationWidth_3 + splitLineHalfWidth) {
        fragColor = splitLineColor;
    }

    if(gl_FragCoord.x > splitScreenFrationWidth_1 + splitScreenFrationWidth_2 + splitScreenFrationWidth_3 + splitScreenFrationWidth_4 - splitLineHalfWidth && gl_FragCoord.x < splitScreenFrationWidth_1 + splitScreenFrationWidth_2 + splitScreenFrationWidth_3 + splitScreenFrationWidth_4 + splitLineHalfWidth) {
        fragColor = splitLineColor;
    }
}




