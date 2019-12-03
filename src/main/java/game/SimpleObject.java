package game;

import audio.OpenALAudioSource;
import interfaces.IF_SceneItem;
import libraries.AudioLibrary;
import rendering.Mesh;
import rendering.ShaderProgram;
import rendering.Transform;

public class SimpleObject implements IF_SceneItem{

    private final Transform transform;
    private final ShaderProgram shader;
    private final Mesh mesh;
    private float glow = 0;


    public SimpleObject(Mesh _mesh, ShaderProgram _shader){
        transform = new Transform();
        shader = _shader;
        mesh = _mesh;
    }

    @Override
    public Transform getTransform(){
        return transform;
    }

    public Mesh getMesh() {
        return mesh;
    }

    @Override
    public ShaderProgram getShader(){
        return shader;
    }

    public void setIlluminationAmount(float _glow) {
        glow = _glow;
    }

    @Override
    public float getIlluminationAmount() {
        return glow;
    }

    @Override
    public void update(float _deltaTime){    }
    public void cleanup()
    {
        mesh.cleanup();
    }


}