package game;

import input.KeyboardInput;
import rendering.Mesh;
import rendering.ShaderProgram;
import rendering.Transform;
import interfaces.IF_SceneItem;

import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;

public class Terrain implements IF_SceneItem {

    private final Transform transform;
    private final ShaderProgram shader;
    private Mesh mesh;
    private float distanceToCamera;
    private float opacity = 1.0f;
    private Random random = new Random();

    private final int lengthRes = 64;
    private final int widthRes = 64;
    private final float length = 50;
    private final float width = 50;
    private float boundingRadius = length;

    private float[] verts;
    private float[] normals;
    private float[] uvCoords;
    private int[] indices;

    public Terrain(ShaderProgram _shader){
        transform = new Transform();
        shader = _shader;

        verts = new float[(lengthRes + 1) * (widthRes + 1) * 3];
        normals = new float[verts.length];
        uvCoords = new float[(lengthRes + 1) * (widthRes + 1) * 2];
        indices = new int[lengthRes * widthRes * 6];

        calcVerts();

        mesh = new Mesh(verts, normals, uvCoords, indices, boundingRadius);

        transform.setPosition(0,0,0);
    }

    private void calcVerts(){

        int index = 0;

        float amplitude = 0.05f;
        float frequency = 1f;

        for(int z = 0; z < lengthRes + 1; z++){
            for(int x = 0; x < widthRes + 1; x++){

                float posX = x * (width / widthRes) - (width / 2);
                float posY = amplitude * random.nextFloat();
                float posZ = z * (length / lengthRes) - (length / 2);

                verts[index + 0] = posX;
                verts[index + 1] = posY;
                verts[index + 2] = posZ;
                index += 3;
            }
        }

        for(int i = 0; i < normals.length; i+=3){
            normals[i+0] = 0f;
            normals[i+1] = 1.0f;
            normals[i+2] = 0f;
        }

        for(int i = 0; i < uvCoords.length; i+=2){
            uvCoords[i] = 0.001f;
            uvCoords[i+1] = 0.26f;
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
    public void setDistanceToCamera(float _distance) {
        distanceToCamera = _distance;
    }

    @Override
    public float getDistanceToCamera() {
        return distanceToCamera;
    }

    @Override
    public float getIlluminationAmount() {
        return 0;
    }

    @Override
    public void setOpacity(float _opacity) {
        opacity = _opacity;
    }

    @Override
    public float getOpacity() {
        return opacity;
    }

    @Override
    public void setIlluminationAmount(float _glow) {

    }

    @Override
    public void update(float _deltaTime) {

        if (KeyboardInput.isKeyRepeated(GLFW_KEY_K)) {
            transform.getPosition().x += 1.0f * _deltaTime;
        }
        else if (KeyboardInput.isKeyRepeated(GLFW_KEY_H)) {
            transform.getPosition().x -= 1.0f * _deltaTime;
        }
        if (KeyboardInput.isKeyRepeated(GLFW_KEY_U)) {
            transform.getPosition().z -= 1.0f * _deltaTime;
        }
        else if (KeyboardInput.isKeyRepeated(GLFW_KEY_J)) {
            transform.getPosition().z += 1.0f * _deltaTime;
        }

    }

    @Override
    public void cleanup() {
        mesh.cleanup();
    }

}
