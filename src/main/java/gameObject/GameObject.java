package gameObject;

import engine.OpenGLMesh;
import math.Vector3f;
import org.joml.Quaternionf;
import utils.Conversions;


public class GameObject {

    private final Vector3f position;
    private final Vector3f scale;
    private final Quaternionf rotation;
    private final OpenGLMesh mesh;

    private boolean isVisible;

    public GameObject(OpenGLMesh mesh)
    {
        position = new Vector3f(0,0,0);
        scale = new Vector3f(1,1,1);
        rotation = new Quaternionf();

        this.mesh = mesh;
        setVisible();
    }

    public void setVisible()
    {
        isVisible = true;
    }

    public void setInvisible()
    {
        isVisible = false;
    }

    public boolean isVisible()
    {
        return isVisible;
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

    public void setScale(Vector3f _scale)
    {
        scale.x = _scale.x;
        scale.y = _scale.y;
        scale.z = _scale.z;
    }

    public void setScale(float _x, float _y, float _z)
    {
        scale.x = _x;
        scale.y = _y;
        scale.z = _z;
    }

    public Vector3f getScale()
    {
        return scale;
    }

    public void setRotation(Quaternionf _rotation)
    {
        rotation.w = _rotation.w;
        rotation.x = _rotation.x;
        rotation.y = _rotation.y;
        rotation.z = _rotation.z;
    }

    public void setRotation(Vector3f _rotation)
    {
        Conversions.convertEulerToQuaternion(_rotation.x, _rotation.y, _rotation.z, rotation);
    }

    public void setRotation(float _x, float _y, float _z)
    {
        Conversions.convertEulerToQuaternion(_x, _y, _z, rotation);
    }

    public Quaternionf getRotation()
    {
        return rotation;
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
