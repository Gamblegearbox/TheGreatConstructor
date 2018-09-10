package game;

import engine.OpenGLMesh;
import engine.Transform;
import interfaces.IF_SceneObject;
import utils.PrototypMeshes;

public class Street implements IF_SceneObject {

    private Transform transform;
    private OpenGLMesh mesh;

    private float anim = 0;

    private int lengthRes = 3;
    private int widthRes = 2;
    private float length = 6f;
    private float width = 2f;

    private float boundingRadius = 2;

    private float[] verts;
    private float[] normals;
    private float[] uvCoords;
    private int[] indices;

    public Street(){
        transform = new Transform();
        transform.setPosition(0,0,-5);

        verts = new float[lengthRes * widthRes * 3];
        normals = new float[lengthRes * widthRes * 3];
        uvCoords = new float[lengthRes * widthRes * 2];
        indices = new int[lengthRes * widthRes * 6];

        calcVerts();

        mesh = new OpenGLMesh(verts, normals, uvCoords, indices, boundingRadius);
    }

    private void calcVerts(){

        int index = 0;
        for(int y = 0; y < lengthRes; y++){
            for(int x = 0; x < widthRes; x++){
                verts[index + 0] = x;
                verts[index + 1] = y;
                verts[index + 2] = 0;
                index += 3;
            }
        }

        for(int i = 0; i < normals.length; i+=3){
            normals[i+0] = 0;
            normals[i+1] = 0;
            normals[i+2] = 1;
        }

        for(int i = 0; i < uvCoords.length; i++){
            uvCoords[i] = 1;
        }

        for (int ti = 0, vi = 0, y = 0; y < lengthRes; y++, vi++) {
            for (int x = 0; x < widthRes; x++, ti += 6, vi++) {
                indices[ti] = vi;
                indices[ti + 3] = indices[ti + 2] = vi + 1;
                indices[ti + 4] = indices[ti + 1] = vi + widthRes + 1;
                indices[ti + 5] = vi + widthRes + 2;
            }
        }
    }

    @Override
    public Transform getTransform() {
        return transform;
    }

    @Override
    public OpenGLMesh getMesh() {
        return mesh;
    }

    @Override
    public void update(float _deltaTime) {
        anim += _deltaTime;

    }

    @Override
    public void cleanup() {
        mesh.cleanup();
    }
}
