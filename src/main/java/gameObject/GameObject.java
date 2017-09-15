package gameObject;

import engine.OpenGLMesh;
import org.joml.Vector3f;

public class GameObject {

    private Vector3f position;
    private Vector3f scale;

    private OpenGLMesh mesh;

    public GameObject(OpenGLMesh mesh)
    {
        position = new Vector3f(0,0,0);
        scale = new Vector3f(1,1,1);
        this.mesh = mesh;
    }

    public void setPosition(Vector3f _position)
    {
        this.position = _position;
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
