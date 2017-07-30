#version 330

in vec2 outTexCoord;
in vec3 mvVertexNormal;
in vec3 mvVertexPos;
in vec4 mlightviewVertexPos;
in mat4 outModelViewMatrix;

out vec4 fragColor;

struct DirectionalLight
{
    vec3 colour;
    vec3 direction;
    float intensity;
};

struct Material
{
    vec3 colour;
    int hasTexture;
    float reflectance;
};

uniform sampler2D texture_sampler;
uniform sampler2D normalMap;
uniform vec3 ambientLight;
uniform float specularPower;
uniform Material material;
uniform DirectionalLight directionalLight;
uniform sampler2D shadowMap;

vec4 calcLightColour(vec3 light_colour, float light_intensity, vec3 position, vec3 to_light_dir, vec3 normal)
{
    vec4 diffuseColour = vec4(0, 0, 0, 0);
    vec4 specColour = vec4(0, 0, 0, 0);

    // Diffuse Light
    float diffuseFactor = max(dot(normal, to_light_dir), 0.0);
    //diffuseFactor = dot(to_light_dir, normalize(normal));
    diffuseColour = vec4(light_colour, 1.0) * light_intensity * diffuseFactor;
    /*if (diffuseFactor > 0.95)
    		diffuseColour = vec4(1.0, 1.0, 1.0, 1.0);
    	else if (diffuseFactor > 0.5)
    		diffuseColour = vec4(0.6, 0.6, 0.6, 1.0);
    	else if (diffuseFactor > 0.25)
    		diffuseColour = vec4(0.4, 0.4, 0.4, 1.0);
    	else
    		diffuseColour = vec4(0.2, 0.2, 0.2, 1.0);
      */
    // Specular Light
    vec3 camera_direction = normalize(-position);
    vec3 from_light_dir = -to_light_dir;
    vec3 reflected_light = normalize(reflect(from_light_dir , normal));
    float specularFactor = max( dot(camera_direction, reflected_light), 0.0);
    specularFactor = pow(specularFactor, specularPower);
    specColour = light_intensity * specularFactor * material.reflectance * vec4(light_colour, 1.0);

    return (diffuseColour + specColour);
}

vec4 calcDirectionalLight(DirectionalLight light, vec3 position, vec3 normal)
{
    return calcLightColour(light.colour, light.intensity, position, normalize(light.direction), normal);
}

vec4 calcBaseColour(Material material, vec2 text_coord)
{
    vec4 baseColour;
    if ( material.hasTexture == 1 )
    {
        baseColour = texture(texture_sampler, text_coord);
    }
    else
    {
        baseColour = vec4(material.colour, 1);
    }
    return baseColour;
}

void main()
{
    vec4 baseColour = calcBaseColour(material, outTexCoord);
    vec3 currNomal = mvVertexNormal;
    vec4 totalLight = calcDirectionalLight(directionalLight, mvVertexPos, currNomal);

    fragColor = baseColour * ( vec4(ambientLight, 1.0) + totalLight);

}