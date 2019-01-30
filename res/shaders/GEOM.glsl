#version 330
layout(triangles) in;
layout(triangle_strip, max_vertices=3) out;

in vec3 vModelSpaceNormal[3];
in vec3 vModelSpacePosition[3];
in vec2 vTexCoord[3];

// output for each vertex emiting
out vec3 gModelSpaceNormal;
out vec3 gModelSpacePosition;
out vec2 gTexCoord;

int enableSmooth=0;


void main(){
	// calculate flat normal
	vec3 oa=vModelSpacePosition[1]-vModelSpacePosition[0];
	vec3 ob=vModelSpacePosition[2]-vModelSpacePosition[0];
	vec3 norm=normalize(cross(oa, ob));

	for(int i=0; i<3; i++){
		// preparing vertex data for emiting
		gl_Position=gl_in[i].gl_Position;
		// switch between flat and smooth shading
		gModelSpaceNormal = enableSmooth == 1 ? vModelSpaceNormal[i] : norm;
		gModelSpacePosition=vModelSpacePosition[i];
		gTexCoord=vTexCoord[i];
		EmitVertex();
	}
	// after assembling...
	EndPrimitive();
}