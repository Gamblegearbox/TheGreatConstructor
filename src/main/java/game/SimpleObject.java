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

    private float rotY = 0.0f;

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

    @Override
    public void update(float _deltaTime){    }
    public void cleanup()
    {
        mesh.cleanup();
    }


}