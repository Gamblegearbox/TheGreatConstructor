package game;

import interfaces.IF_SceneItem;
import rendering.Mesh;
import rendering.ShaderProgram;
import rendering.Transform;

public class SimpleObject implements IF_SceneItem{

    private final Transform transform;
    private final ShaderProgram shader;
    private final Mesh mesh;
    private float illuminationAmount = 1;
    private float distanceToCamera;
    private float opacity = 1.0f;


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
    public void setDistanceToCamera(float _distance) {
        distanceToCamera = _distance;
    }

    @Override
    public float getDistanceToCamera() {
        return distanceToCamera;
    }

    public void setIlluminationAmount(float _glow) {
        illuminationAmount = _glow;
    }

    @Override
    public float getIlluminationAmount() {
        return illuminationAmount;
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
    public void update(float _deltaTime){    }

    public void cleanup()
    {
        mesh.cleanup();
    }

}