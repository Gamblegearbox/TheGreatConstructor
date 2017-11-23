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

    public void mul(Vector4 _vector, Matrix4 _matrix)
    {
        float[] m = _matrix.getValues();
        float x1 = _vector.x;
        float y1 = _vector.y;
        float z1 = _vector.z;
        float w1 = _vector.w;

        x = m[0] * x1 + m[4] * y1 + m[8]  * z1 + m[12] * w1;
        y = m[1] * x1 + m[5] * y1 + m[9]  * z1 + m[13] * w1;
        z = m[2] * x1 + m[6] * y1 + m[10] * z1 + m[14] * w1;
        w = m[3] * x1 + m[7] * y1 + m[11] * z1 + m[15] * w1;
    }
}
