#version 330
#extension GL_EXT_gpu_shader4 : enable
#extension GL_EXT_geometry_shader4 : enable

layout(triangles) in;
layout(triangle_strip, max_vertices=3) out;

in vec3 vPos_ObjectSpace[3];
in vec3 vPos_WorldSpace[3];
in vec3 vPos_ViewSpace[3];

in vec3 vNormal_ObjectSpace[3];
in vec3 vNormal_WorldSpace[3];
in vec3 vNormal_ViewSpace[3];

in vec2 vTexCoord[3];

// output for each vertex emiting
out vec3 gPos_ObjectSpace;
out vec3 gPos_WorldSpace;
out vec3 gPos_ViewSpace;

out vec3 gNormal_ObjectSpace;
out vec3 gNormal_WorldSpace;
out vec3 gNormal_ViewSpace;

out vec2 gTexCoord;

out vec3 gDistanceToEdge;

//TODO:uniform
const vec2 WIN_SCALE = vec2(1280,720);

//REFERENCES
//SINGLE PASS WIREFRAME: http://strattonbrazil.blogspot.com/2011/09/single-pass-wireframe-rendering_10.html
void main(){
    int enableSmooth=1;
    vec3 oa = vPos_WorldSpace[1] - vPos_WorldSpace[0];
    vec3 ob = vPos_WorldSpace[2] - vPos_WorldSpace[0];
    vec3 normal_Flatshading = normalize(cross(oa, ob));

    vec2 p0 = WIN_SCALE * gl_PositionIn[0].xy/gl_PositionIn[0].w;
    vec2 p1 = WIN_SCALE * gl_PositionIn[1].xy/gl_PositionIn[1].w;
    vec2 p2 = WIN_SCALE * gl_PositionIn[2].xy/gl_PositionIn[2].w;
    vec2 v0 = p2-p1;
    vec2 v1 = p2-p0;
    vec2 v2 = p1-p0;
    float area = abs(v1.x * v2.y - v1.y * v2.x);

    //VERTEX 0
    gDistanceToEdge = vec3(area / length(v0),0,0);

    gPos_ObjectSpace = vPos_ObjectSpace[0];
    gPos_WorldSpace = vPos_WorldSpace[0];
    gPos_ViewSpace = vPos_ViewSpace[0];

    gNormal_ObjectSpace = vNormal_ObjectSpace[0];
    gNormal_WorldSpace = enableSmooth == 1 ? vNormal_WorldSpace[0] : normal_Flatshading;
    gNormal_ViewSpace = vNormal_ViewSpace[0];

    gTexCoord=vTexCoord[0];
    gl_Position = gl_PositionIn[0];
    EmitVertex();

    //VERTEX 1
    gDistanceToEdge = vec3(0,area/length(v1),0);

    gPos_ObjectSpace = vPos_ObjectSpace[1];
    gPos_WorldSpace = vPos_WorldSpace[1];
    gPos_ViewSpace = vPos_ViewSpace[1];

    gNormal_ObjectSpace = vNormal_ObjectSpace[1];
    gNormal_WorldSpace = enableSmooth == 1 ? vNormal_WorldSpace[1] : normal_Flatshading;
    gNormal_ViewSpace = vNormal_ViewSpace[1];

    gTexCoord=vTexCoord[1];
    gl_Position = gl_PositionIn[1];
    EmitVertex();

    //VERTEX 2
    gDistanceToEdge = vec3(0,0,area/length(v2));

    gPos_ObjectSpace = vPos_ObjectSpace[2];
    gPos_WorldSpace = vPos_WorldSpace[2];
    gPos_ViewSpace = vPos_ViewSpace[2];

    gNormal_ObjectSpace = vNormal_ObjectSpace[2];
    gNormal_WorldSpace = enableSmooth == 1 ? vNormal_WorldSpace[2] : normal_Flatshading;
    gNormal_ViewSpace = vNormal_ViewSpace[2];

    gTexCoord=vTexCoord[2];
    gl_Position = gl_PositionIn[2];
    EmitVertex();

    EndPrimitive();
}