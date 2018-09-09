package game;

import engine.Transform;
import engine.OpenGLMesh;
import interfaces.IF_SceneObject;

public class Player implements IF_SceneObject {

    float x = 0;
    float z = 0;
    float y = 0;
    float speed = 5.0f;

    Transform transform;
    OpenGLMesh mesh;

    public Player(OpenGLMesh _mesh){
        transform = new Transform();
        mesh = _mesh;
    }

    @Override
    public Transform getTransform(){
        return transform;
    }

    @Override
    public void update(float _deltaTime){


        x = 3f;
        y += _deltaTime * speed;
        z = -10f;

        if(y > 8){
            y = -8;
        }


        transform.setPosition(x,y,z);
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
