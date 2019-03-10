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
    float AMPLITUDE = 1;
    float AMPLITUDE_2 = 0.3;
    float WAVE_LENGTH = 0.8;
    float WAVE_LENGTH_2 = 0.3;
    float SPEED = 1;
    float SPEED_2 = 3.5;
    temp.y += (AMPLITUDE * sin(WAVE_LENGTH * (SPEED * anima + temp.x)) + (AMPLITUDE_2 * cos(WAVE_LENGTH_2 * (SPEED_2 * anima + temp.x + temp.z))));

    return temp;
}

//WAVE USING GERSTNER WAVES
vec3 gerstnerWave(vec3 _position){
    //SOURCE: https://catlikecoding.com/unity/tutorials/flow/waves/
    vec3 temp = _position;
    float PI = 3.1415926535897932384626433832795;
    float STEEPNESS = 0.6; //between 0.0 and 1.0
    float WAVE_LENGTH = 5;
    float SPEED = 1;

    float k = 2.0 * PI / WAVE_LENGTH;
    float f = k * temp.x + SPEED * anima;
    float a = STEEPNESS / k;

    temp.x += a * cos(f);
    temp.y = a * sin(f);

    return temp;
}

void main()
{
    bool useSineWave = true;
    vec3 positionWithWave = (useSineWave) ? sineWave(position) : gerstnerWave(position);

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
