package gameObject;

import engine.OpenGLMesh;
import math.Vector3;
import org.joml.Quaternionf;
import utils.Conversions;


public class GameObject {

    private final Vector3 position;
    private final Vector3 scale;
    private final Quaternionf rotation;
    private final OpenGLMesh mesh;

    private final float boundingRadius;
    private boolean isVisible;

    public GameObject(OpenGLMesh _mesh, float _boundingRadius)
    {
        position = new Vector3(0,0,0);
        scale = new Vector3(1,1,1);
        rotation = new Quaternionf();
        boundingRadius = _boundingRadius;
        this.mesh = _mesh;
        setVisibility(true);
    }

    public void setVisibility(boolean _value)
    {
        isVisible = _value;
    }

    public boolean isVisible()
    {
        return isVisible;
    }

    public float getBoundingRadius()
    {
        return boundingRadius;
    }

    public void setPosition(Vector3 _position)
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

    public Vector3 getPosition()
    {
        return position;
    }

    public void setScale(Vector3 _scale)
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

    public Vector3 getScale()
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

    public void setRotation(Vector3 _rotation)
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
