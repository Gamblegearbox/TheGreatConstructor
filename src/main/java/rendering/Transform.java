package rendering;


import org.joml.Vector3f;

public class Transform {

    private final Vector3f position;
    private final Vector3f scale;
    private final Vector3f rotation;

    private boolean isStatic;

    public Transform()
    {
        position = new Vector3f(0,0,0);
        scale = new Vector3f(1,1,1);
        rotation = new Vector3f(0,0,0);
    }

    public void setIsStatic(boolean _value){
        isStatic = _value;
    }

    public boolean isStatic(){
        return isStatic;
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

    public void setRotation(Vector3f _rotation)
    {
        rotation.x = _rotation.x;
        rotation.y = _rotation.y;
        rotation.z = _rotation.z;
    }

    public void setRotation(float _x, float _y, float _z)
    {
        rotation.x = _x;
        rotation.y = _y;
        rotation.z = _z;
    }
/*
    public void setRotation(Vector3f _rotation)
    {
        Conversions.convertEulerToQuaternion(_rotation.x, _rotation.y, _rotation.z, rotation);
    }
*/
/*
    public void setRotation(float _x, float _y, float _z)
    {
        Conversions.convertEulerToQuaternion(_x, _y, _z, rotation);
    }
*/
    public Vector3f getRotation()
    {
        return rotation;
    }


}
