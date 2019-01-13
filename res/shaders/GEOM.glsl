#version 330
layout(triangles) in;
layout(triangle_strip, max_vertices=3) out;


in vec3 vNorm[3];
in vec3 vWorldPos[3];
in vec2 vTexCoord[3];

// output for each vertex emiting
out vec3 gNorm;
out vec3 gWorldPos;
out vec2 gTexCoord;

int enableSmooth=0;


void main(){
	// calculate flat normal
	vec3 oa=vWorldPos[1]-vWorldPos[0];
	vec3 ob=vWorldPos[2]-vWorldPos[0];
	vec3 norm=normalize(cross(oa, ob));
	for(int i=0; i<3; i++){
		// preparing vertex data for emiting
		gl_Position=gl_in[i].gl_Position;
		// switch between flat and smooth shading
		gNorm=enableSmooth==1? vNorm[i]: norm;
		gWorldPos=vWorldPos[i];
		gTexCoord=vTexCoord[i];
		EmitVertex();
	}
	// after assembling...
	EndPrimitive();
}