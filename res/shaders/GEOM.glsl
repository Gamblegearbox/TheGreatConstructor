#version 330
layout(triangles) in;
layout(triangle_strip, max_vertices=3) out;


in vec3 vNormal[3];
in vec3 vPosition[3];
in vec2 vTexCoord[3];

// output for each vertex emiting
out vec3 gNormal;
out vec3 gPosition;
out vec2 gTexCoord;

int enableSmooth=0;


void main(){
	// calculate flat normal
	vec3 oa=vPosition[1]-vPosition[0];
	vec3 ob=vPosition[2]-vPosition[0];
	vec3 norm=normalize(cross(oa, ob));

	for(int i=0; i<3; i++){
		// preparing vertex data for emiting
		gl_Position=gl_in[i].gl_Position;
		// switch between flat and smooth shading
		gNormal=enableSmooth==1? vNormal[i]: norm;
		gPosition=vPosition[i];
		gTexCoord=vTexCoord[i];
		EmitVertex();
	}
	// after assembling...
	EndPrimitive();
}