package math;

public class Vector4 {

    public float x;
    public float y;
    public float z;
    public float w;

    public Vector4()
    {
        x = 0;
        y = 0;
        z = 0;
        w = 0;
    }

    public Vector4(float _x, float _y, float _z, float _w)
    {
        x = _x;
        y = _y;
        z = _z;
        w = _w;
    }

    public Vector4(Vector4 _value)
    {
        x = _value.x;
        y = _value.y;
        z = _value.z;
        w = _value.w;
    }

    public void set(float _x, float _y, float _z, float _w)
    {
        x = _x;
        y = _y;
        z = _z;
        w = _w;
    }

    public void set(Vector4 _value)
    {
        x = _value.x;
        y = _value.y;
        z = _value.z;
        w = _value.w;
    }

    public String toString()
    {
        return "x: " + x + " | y: " + y + " | z: " + z + " | w: " + w;
    }

    //FROM JOML VECTOR4F
    public void normalize3()
    {
        float invLength = (float) (1.0 / Math.sqrt(x * x + y * y + z * z));
        x *= invLength;
        y *= invLength;
        z *= invLength;
        w *= invLength;
    }
}
