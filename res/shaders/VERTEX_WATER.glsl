#version 430
#pragma debug(on)
#pragma optimize(off)

#define PI 3.141592
#define USE_SINE 1
#define USE_NOISE 1
#define USE_GERSTNER 1

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


vec3 sineWave(vec4 _wave, vec3 _position){

    float AMPLITUDE = _wave.x;
    float WAVE_LENGTH = _wave.y;
    float SPEED = _wave.z;
    float DIRECTION = _wave.w;

    float y = (AMPLITUDE * sin(WAVE_LENGTH * (SPEED * anima + DIRECTION)));

    return vec3(0, y, 0);
}

vec3 gerstnerWave(vec4 wave, vec3 _position){
    //SOURCE: https://catlikecoding.com/unity/tutorials/flow/waves/

    float steepness = wave.z;
    float wavelength = wave.w;

    float k = 2.0 * PI / wavelength;
    float c = sqrt(3.0 / k);//correct formula: sqrt(9.8 / k); but waves are quit fast
    vec2 d = normalize(wave.xy);
    float f = k * dot(d, _position.xz) + c * anima;
    float a = steepness / k;

    return vec3(d.x * (a * cos(f)), a * sin(f), d.y * (a * cos(f)));
}

float random1D(float _in){
    // SOURCE: https://thebookofshaders.com/10/
    return fract(sin(_in) * 1.0);
}

float random2D(vec2 _input){
    // SOURCE: https://thebookofshaders.com/10/
    return fract(sin(dot(_input, vec2(12.9898,78.233))) * 43758.5453123);
}

vec3 noise2D(float _height, vec3 _position){
    // SOURCE: https://thebookofshaders.com/11/
    vec2 base = _position.xz;
    vec2 i = floor(base);
    vec2 f = fract(base);

    //Corner values
    float a = random2D(i);
    float b = random2D(i + vec2(1.0, 0.0));
    float c = random2D(i + vec2(0.0, 1.0));
    float d = random2D(i + vec2(1.0, 1.0));

    //Cubic interpolation
    vec2 u = f * f * f * (f *(f * 6.0 - 15.0) + 10.0);

    //Mix
    float y = mix(a, b, u.x) + (c -a) * u.y * (1.0 - u.x) + (d - b) * u.x * u.y;

    return vec3(0, y * -_height, 0);
}

void main()
{
    vec3 positionWithWave = position;

    #if USE_SINE == 1
    //WAVE PARAMTERS: vec4(Amplitude, WaveLength, Speed, Direction)
    vec4 sineWave1 = vec4(0.5, 0.2, 6.1, position.x);
    vec4 sineWave2 = vec4(0.5, 0.1, 8.0, position.z);
    positionWithWave += sineWave(sineWave1, position);
    positionWithWave += sineWave(sineWave2, position);
    #endif

    #if USE_NOISE == 1
    positionWithWave += noise2D(0.25, position * 0.2 - anima * 0.2);
    positionWithWave += noise2D(0.5, position * 0.15 + anima * 0.55);
    #endif

    #if USE_GERSTNER == 1
    //vec4(dir.x, dir.y, steepness(0.0 - 1.0), wavelength)
    //steepness of all waves should not exeed 1.0
    vec4 gerstnerWave1 = vec4(0.65, 0.35, 0.2, 12.0);
    vec4 gerstnerWave2 = vec4(0.8, 0.2, 0.3, 8.0);
    positionWithWave += gerstnerWave(gerstnerWave1, position);
    positionWithWave += gerstnerWave(gerstnerWave2, position);
    #endif

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
