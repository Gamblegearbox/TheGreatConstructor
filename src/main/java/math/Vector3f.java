package math;

public class Vector3f {

    public float x;
    public float y;
    public float z;

    public Vector3f(float _x, float _y, float _z)
    {
        x = _x;
        y = _y;
        z = _z;
    }

    public Vector3f(Vector3f value)
    {
        x = value.x;
        y = value.y;
        z = value.z;
    }

    public String toString()
    {
        return "x: " + x + " | y: " + y + " | z: " + z;
    }
}
