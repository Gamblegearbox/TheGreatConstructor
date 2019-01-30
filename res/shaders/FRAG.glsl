#version 330

in vec3 gModelSpacePosition;
in vec3 gModelSpaceNormal;
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
    //TEXTURE
    vec4 textureColor = texture(diffuseMap_sampler, gTexCoord);

    //NORMAL INFORMATION AS BLACK AND WHITE
    float normalValue = (gModelSpaceNormal.r + gModelSpaceNormal.g + gModelSpaceNormal.b) / 3.0 ;
    vec4 normalAsBlackAndWhite = vec4(normalValue, normalValue, normalValue, 1.0);

    //OUT COLOR
    vec4 finalColor;
    if(texture(illuminationMap_sampler, gTexCoord).a > 0.0){
        finalColor = textureColor;
    }
    else {
        finalColor = textureColor * normalAsBlackAndWhite;
    }

    fragColor = clamp(finalColor,0.0,1.0);

}




