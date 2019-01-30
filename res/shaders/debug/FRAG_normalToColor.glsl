#version 330

in vec3 gModelSpaceNormal;
in vec3 gModelSpacePosition;
in vec2 gTexCoord;

out vec4 fragColor;

uniform vec3 lightPosition;
uniform bool hasNormalMap;
uniform sampler2D normalMap_sampler;

void main()
{
    vec4 finalColor;
    vec3 normalColor;

    //CALC NORMAL
    vec3 currentNormal;

    if(hasNormalMap) {
        currentNormal = normalize(texture(normalMap_sampler, gTexCoord).rgb * 2.0 - 1.0);
        currentNormal = normalize(gModelSpaceNormal + currentNormal);
    }
    else {
        currentNormal = gModelSpaceNormal;
    }

    float r = (currentNormal.r + 1.0) * 0.5;
    float g = (currentNormal.g + 1.0) * 0.5;
    float b = (currentNormal.b + 1.0) * 0.5;

    finalColor.rgba = vec4(r,g,b, 1.0);

    fragColor = clamp(finalColor,0,1);
}




