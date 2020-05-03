package game;

import rendering.Mesh;
import rendering.ShaderProgram;
import rendering.Transform;
import interfaces.IF_SceneItem;

public class Water implements IF_SceneItem {

    private final Transform transform;
    private final ShaderProgram shader;
    private final Mesh mesh;
    private float distanceToCamera;
    private float opacity = 1.0f;

    public Water(float _width, float _length, int _resWidth, int _resLength, ShaderProgram _shader){
        transform = new Transform();
        mesh = utils.MeshBuilder.createPlane(_width, _length, _resWidth, _resLength, 3, 11);

        shader = _shader;
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
    public void update(float _deltaTime) {    }

    @Override
    public void cleanup() {
        mesh.cleanup();
    }


}
