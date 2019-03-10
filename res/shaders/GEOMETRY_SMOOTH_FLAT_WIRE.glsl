#version 330
#extension GL_EXT_gpu_shader4 : enable
#extension GL_EXT_geometry_shader4 : enable

layout(triangles) in;
layout(triangle_strip, max_vertices=3) out;

in vec3 vWorldSpacePosition[3];
in vec3 vWorldSpaceNormal[3];
in vec2 vTexCoord[3];

// output for each vertex emiting
out vec3 gWorldSpacePosition;
out vec3 gWorldSpaceNormal;
out vec2 gTexCoord;
out vec3 gDistanceToEdge;

//TODO:uniform
const vec2 WIN_SCALE = vec2(1280,720);

//REFERENCES
//SINGLE PASS WIREFRAME: http://strattonbrazil.blogspot.com/2011/09/single-pass-wireframe-rendering_10.html
void main(){
    int enableSmooth=0;
    vec3 oa=vWorldSpacePosition[1]-vWorldSpacePosition[0];
    vec3 ob=vWorldSpacePosition[2]-vWorldSpacePosition[0];
    vec3 norm=normalize(cross(oa, ob));

	vec2 p0 = WIN_SCALE * gl_PositionIn[0].xy/gl_PositionIn[0].w;
	vec2 p1 = WIN_SCALE * gl_PositionIn[1].xy/gl_PositionIn[1].w;
    vec2 p2 = WIN_SCALE * gl_PositionIn[2].xy/gl_PositionIn[2].w;
    vec2 v0 = p2-p1;
    vec2 v1 = p2-p0;
    vec2 v2 = p1-p0;
    float area = abs(v1.x*v2.y - v1.y * v2.x);

	gDistanceToEdge = vec3(area/length(v0),0,0);
    gWorldSpacePosition = vWorldSpacePosition[0];
    gWorldSpaceNormal = enableSmooth == 1 ? vWorldSpaceNormal[0] : norm;
    gTexCoord=vTexCoord[0];
    gl_Position = gl_PositionIn[0];
    EmitVertex();

    gDistanceToEdge = vec3(0,area/length(v1),0);
    gWorldSpacePosition = vWorldSpacePosition[1];
    gWorldSpaceNormal = enableSmooth == 1 ? vWorldSpaceNormal[1] : norm;
    gTexCoord=vTexCoord[1];
    gl_Position = gl_PositionIn[1];
    EmitVertex();

    gDistanceToEdge = vec3(0,0,area/length(v2));
    gWorldSpacePosition = vWorldSpacePosition[2];
    gWorldSpaceNormal = enableSmooth == 1 ? vWorldSpaceNormal[2] : norm;
    gTexCoord=vTexCoord[2];
    gl_Position = gl_PositionIn[2];
    EmitVertex();

    EndPrimitive();
}