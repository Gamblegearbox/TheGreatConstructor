package game;

import rendering.OpenGLMesh;
import core.Transform;
import interfaces.IF_SceneObject;

public class Water implements IF_SceneObject {

    private Transform transform;
    private OpenGLMesh mesh;

    private float anim = 0;

    private final int lengthRes = 50;
    private final int widthRes = 50;
    private final float length = 43.3f;
    private final float width = 45f;

    private float waveHeight_1 = 0.6f;
    private float waveHeight_2 = 0.25f;

    private float boundingRadius = 2;

    private float[] verts;
    private float[] normals;
    private float[] uvCoords;
    private int[] indices;

    public Water(){
        transform = new Transform();

        verts = new float[(lengthRes + 1) * (widthRes + 1) * 3];
        normals = new float[verts.length];
        uvCoords = new float[(lengthRes + 1) * (widthRes + 1) * 2];
        indices = new int[lengthRes * widthRes * 6];

        calcVerts();

        mesh = new OpenGLMesh(verts, normals, uvCoords, indices, boundingRadius);
        transform.setPosition(20,-3,0);
    }

    private void calcVerts(){

        int index = 0;
        for(int z = 0; z < lengthRes + 1; z++){
            for(int x = 0; x < widthRes + 1; x++){
                verts[index + 0] = (x * (width / widthRes)) - (width / 2);
                verts[index + 1] = (float)Math.sin(x + anim) * waveHeight_1 + (float)Math.cos((x+z)/2 + anim) * waveHeight_2;
                verts[index + 2] = z * (length / lengthRes) - (length / 2);
                index += 3;
            }
        }

        for(int i = 0; i < normals.length; i+=3){
            normals[i+0] = 0f;
            normals[i+1] = 1f;
            normals[i+2] = 0f;
        }

        for(int i = 0; i < uvCoords.length; i+=2){
            uvCoords[i] = 0.03125f + 0.0625f * 3f;
            uvCoords[i+1] = 0.03125f + 0.0625f * 3f;
        }

        for (int ti = 0, vi = 0, y = 0; y < lengthRes; y++, vi++) {
            for (int x = 0; x < widthRes; x++, ti += 6, vi++) {
                indices[ti + 0] = vi + 0;
                indices[ti + 1] = indices[ti + 4] = vi + widthRes + 1;
                indices[ti + 2] = indices[ti + 3] = vi + 1;
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



        int index = 0;
        for(int z = 0; z < lengthRes + 1; z++){
            for(int x = 0; x < widthRes + 1; x++){
                verts[index + 0] = (x * (width / widthRes)) - (width / 2);
                verts[index + 1] = (float)Math.sin(x + anim) * waveHeight_1 + (float)Math.cos((x+z)/2 + anim) * waveHeight_2;
                verts[index + 2] = z * (length / lengthRes) - (length / 2);
                index += 3;
            }
        }

        mesh.updateVertices(verts);

    }

    @Override
    public void cleanup() {
        mesh.cleanup();
    }
}
