package game;

import rendering.Mesh;
import rendering.Transform;
import interfaces.IF_SceneObject;

public class Logo implements IF_SceneObject {

    float time = 0;
    float fastTime = 0;

    private Transform transform;
    private Mesh mesh;

    public Logo() {
        transform = new Transform();
        mesh = Assets.LOGO;
    }

    @Override
    public Transform getTransform(){
        return transform;
    }

    @Override
    public Mesh getMesh() {
        return mesh;
    }

    @Override
    public void update(float _deltaTime) {

        time += _deltaTime;
        fastTime += _deltaTime * 2;
        float sin = (float)Math.sin(time);
        float fastSin = (float)Math.sin(fastTime);

        transform.setPosition(sin, 0, sin);
        transform.setRotation(fastSin*5, sin*20, sin*5);
    }

    public void cleanup()
    {
        mesh.cleanup();
    }
}
