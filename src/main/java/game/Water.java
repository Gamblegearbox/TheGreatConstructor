package game;

import rendering.Mesh;
import rendering.ShaderProgram;
import rendering.Transform;
import interfaces.IF_SceneItem;

public class Water implements IF_SceneItem {

    private final Transform transform;
    private final ShaderProgram shader;
    private Mesh mesh;

    private final int lengthRes = 50;
    private final int widthRes = 50;
    private final float length = 43f;
    private final float width = 45f;

    private float boundingRadius = 30;

    private float[] verts;
    private float[] normals;
    private float[] uvCoords;
    private int[] indices;


    public Water(ShaderProgram _shader){
        transform = new Transform();

        verts = new float[(lengthRes + 1) * (widthRes + 1) * 3];
        normals = new float[verts.length];
        uvCoords = new float[(lengthRes + 1) * (widthRes + 1) * 2];
        indices = new int[lengthRes * widthRes * 6];

        calcVerts();

        mesh = new Mesh(verts, normals, uvCoords, indices, boundingRadius);
        transform.setPosition(20,-3,0);
        shader = _shader;
    }

    private void calcVerts(){

        int index = 0;
        for(int z = 0; z < lengthRes + 1; z++){
            for(int x = 0; x < widthRes + 1; x++){
                verts[index + 0] = (x * (width / widthRes)) - (width / 2);
                verts[index + 1] = 0f;
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
    public Mesh getMesh() {
        return mesh;
    }

    @Override
    public ShaderProgram getShader(){
        return shader;
    }


    @Override
    public void update(float _deltaTime) {    }

    @Override
    public void cleanup() {
        mesh.cleanup();
    }
}
