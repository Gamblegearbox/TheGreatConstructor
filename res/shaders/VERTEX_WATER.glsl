#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec3 normal;
layout (location=2) in vec2 uvCoord;

out vec3 vWorldSpacePosition;
out vec3 vWorldSpaceNormal;
out vec2 vTexCoord;

uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;
uniform mat4 modelMatrix;
uniform float anima;

//WAVE USING SINE FUNCTION
vec3 sineWave(vec3 _position){

    vec3 temp = _position;
    float AMPLITUDE = 0.8;
    float AMPLITUDE_2 = 0.3;
    float WAVE_LENGTH = 0.8;
    float WAVE_LENGTH_2 = 1.0;
    float SPEED = 3.5;
    float SPEED_2 = 2.0;
    temp.y += (AMPLITUDE * sin(WAVE_LENGTH * (SPEED * anima + temp.x)) + (AMPLITUDE_2 * cos(WAVE_LENGTH_2 * (SPEED_2 * anima + temp.x + temp.z))));

    return temp;
}

//WAVE USING GERSTNER WAVES
vec3 gerstnerWave(vec4 wave, vec3 _position){
    //SOURCE: https://catlikecoding.com/unity/tutorials/flow/waves/
    float PI = 3.1415926535897932384626433832795;

    float steepness = wave.z;
    float wavelength = wave.w;

    float k = 2.0 * PI / wavelength;
    float c = sqrt(9.8 / k);
    vec2 d = normalize(wave.xy);
    float f = k * dot(d, _position.xz) + c * anima;
    float a = steepness / k;

    return vec3(d.x * (a * cos(f)), a * sin(f), d.y * (a * cos(f)));
}

void main()
{
    bool useSineWave = false;
    vec3 positionWithWave = position;

    if(useSineWave){
        positionWithWave = sineWave(position);
    }
    else{
        //vec4(dir.x, dir.y, steepness(0.0 - 1.0), wavelength)
        vec4 wave1 = vec4(0.9, 0.1, 0.2, 2.0);
        vec4 wave2 = vec4(0.8, 0.2, 0.4, 8.0);
        vec4 wave3 = vec4(0.7, 1.0, 0.3, 4.0);
        positionWithWave += gerstnerWave(wave1, position);
        positionWithWave += gerstnerWave(wave2, position);
        positionWithWave += gerstnerWave(wave3, position);
    }


    vec4 vertexWorldSpacePos = modelMatrix * vec4(positionWithWave, 1.0);
    vec4 vertexViewSpacePos = viewMatrix * vertexWorldSpacePos;

    vec4 normalWorldSpacePos = modelMatrix * vec4(normal, 1.0);
    vec4 normalViewSpacePos = viewMatrix * normalWorldSpacePos;

    //OUT
    vWorldSpaceNormal = vec3(modelMatrix * vec4(normal, 0.0));
    vWorldSpacePosition = vertexWorldSpacePos.xyz;
    vTexCoord = uvCoord;

    //GL OUT
    gl_Position = projectionMatrix * vertexViewSpacePos;
}
