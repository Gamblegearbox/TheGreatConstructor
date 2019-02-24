#version 330

in vec2 vTexCoord;

out vec4 fragColor;

uniform sampler2D diffuseMap_sampler;
uniform vec4 fontColor;
uniform vec4 labelColor;

void main()
{

    fragColor = fontColor * texture(diffuseMap_sampler, vTexCoord);
}