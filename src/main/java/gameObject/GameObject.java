package gameObject;

import engine.OpenGLMesh;
import math.Vector3f;
import org.joml.Quaternionf;


public class GameObject {

    private final Vector3f position;
    private final Vector3f scale;
    private final Quaternionf rotation;
    private final OpenGLMesh mesh;

    public GameObject(OpenGLMesh mesh)
    {
        position = new Vector3f(0,0,0);
        scale = new Vector3f(1,1,1);
        rotation = new Quaternionf();

        this.mesh = mesh;
    }

    public void setPosition(Vector3f _position)
    {
        position.x = _position.x;
        position.y = _position.y;
        position.z = _position.z;
    }

    public void setPosition(float _x, float _y, float _z)
    {
        position.x = _x;
        position.y = _y;
        position.z = _z;
    }

    public Vector3f getPosition()
    {
        return position;
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
