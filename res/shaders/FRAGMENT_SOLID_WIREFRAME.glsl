#version 330

in vec3 gWorldSpacePosition;
in vec3 gWorldSpaceNormal;
in vec2 gTexCoord;
in vec3 gDistanceToEdge;

out vec4 fragColor;

uniform vec3 lightPosition;
uniform float timeOfDay;
uniform vec3 cameraPosition;

void main()
{
    vec4 colorAmbient = vec4(0.5,0.5,0.5,1.0);
    vec4 colorDiffuse = vec4(1.0,1.0,1.0,1.0);

    vec4 colorWireframe = vec4(0.1,0.1,0.1,1.0);

    vec3 toLightSource = normalize(lightPosition);
    float diffuseFactor = max(dot(gWorldSpaceNormal, toLightSource), 0.0);
    colorDiffuse.rgb *= diffuseFactor;

    float nearD = min(min(gDistanceToEdge[0],gDistanceToEdge[1]),gDistanceToEdge[2]);
    float edgeIntensity = exp2(-1.0*nearD*nearD);

    fragColor = (edgeIntensity * colorWireframe) + ((1.0-edgeIntensity) * (colorDiffuse + colorAmbient));

}