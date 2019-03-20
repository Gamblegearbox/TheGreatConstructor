package game;

import rendering.Mesh;
import rendering.ShaderProgram;
import rendering.Transform;
import interfaces.IF_SceneItem;

public class Water implements IF_SceneItem {

    private final Transform transform;
    private final ShaderProgram shader;
    private Mesh mesh;

    public Water(float _width, float _length, int _resWidth, int _resLength, ShaderProgram _shader){
        transform = new Transform();
        mesh = utils.MeshBuilder.createPlane(_width, _length, _resWidth, _resLength);

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
    public void update(float _deltaTime) {    }

    @Override
    public void cleanup() {
        mesh.cleanup();
    }
}
