package math;

public class Vector3 {

    public static final Vector3 ZERO = new Vector3(0f,0f,0f);
    public static final Vector3 ONE = new Vector3(1f,1f,1f);

    public float x;
    public float y;
    public float z;

    public Vector3(float _x, float _y, float _z)
    {
        x = _x;
        y = _y;
        z = _z;
    }

    public Vector3(Vector3 _value)
    {
        x = _value.x;
        y = _value.y;
        z = _value.z;
    }

    public void set(float _x, float _y, float _z)
    {
        x = _x;
        y = _y;
        z = _z;
    }

    public void set(Vector3 _value)
    {
        x = _value.x;
        y = _value.y;
        z = _value.z;
    }

    public String toString()
    {
        return "x: " + x + " | y: " + y + " | z: " + z;
    }
}
