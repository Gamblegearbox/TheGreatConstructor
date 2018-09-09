package game;

import engine.OpenGLMesh;
import engine.Transform;
import interfaces.IF_SceneObject;

public class Logo implements IF_SceneObject {

    float time = 0;
    float fastTime = 0;

    private Transform transform;
    private OpenGLMesh mesh;

    public Logo(OpenGLMesh _mesh) {
        transform = new Transform();
        mesh = _mesh;
    }

    @Override
    public Transform getTransform(){
        return transform;
    }

    @Override
    public OpenGLMesh getMesh() {
        return mesh;
    }

    @Override
    public void update(float _deltaTime) {

        time += _deltaTime;
        fastTime += _deltaTime * 2;
        float sin = (float)Math.sin(time);
        float fastSin = (float)Math.sin(fastTime);

        transform.setPosition(sin, 0, sin-15);
        transform.setRotation(fastSin*5, sin*20, sin*5);
    }

    public void cleanup()
    {
        mesh.cleanup();
    }
}
