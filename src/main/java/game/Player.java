package game;

import engine.MeshLibrary;
import engine.Transform;
import engine.OpenGLMesh;
import interfaces.IF_SceneObject;

public class Player implements IF_SceneObject {

    float x = 0;
    float z = 0;
    float y = 0;
    float speed = 15.0f;

    Transform transform;
    OpenGLMesh mesh;

    public Player(){
        mesh = MeshLibrary.getMeshByTag("Coupe");

        transform = new Transform();
        transform.setPosition(2,-2,-7);
    }

    @Override
    public Transform getTransform(){
        return transform;
    }

    @Override
    public void update(float _deltaTime){

        y += _deltaTime * speed;

        transform.setRotation(0,y,0);
    }

    public OpenGLMesh getMesh()
    {
        return mesh;
    }

    public void cleanup()
    {
        mesh.cleanup();
    }
}
