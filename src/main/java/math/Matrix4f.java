package math;

public class Matrix4f {

    private float[] values;

    public Matrix4f()
    {
        values = new float[16];

        for(int i = 0; i < values.length; i++)
        {
            values[i] = 0;
        }
    }

    public float[] getValues()
    {
        return values;
    }

    public Matrix4f perspective(float _fovInDegrees, float _aspectRatio, float _zNear, float _zFar)
    {
        float fov = (float) Math.toRadians(_fovInDegrees);
        float aspectRatio = _aspectRatio;
        float zNear = _zNear;
        float zFar = _zFar;

        float magicFov = 1.0f / (float)Math.tan(fov / 2.0f);
        float zm = zFar - zNear;
        float zp = zFar + zNear;

        values[0] = magicFov / aspectRatio; values[4] = 0;          values[8] = 0;          values[12] = 0;
        values[1] = 0;                      values[5] = magicFov;   values[9] = 0;          values[13] = 0;
        values[2] = 0;                      values[6] = 0;          values[10] = -zp / zm;  values[14] = -(2 * zFar * zNear) / zm;
        values[3] = 0;                      values[7] = 0;          values[11] = -1;        values[15] = 0;

        return this;
    }

    public Matrix4f identity()
    {
        values[0] = 1;    values[4] = 0;     values[8] = 0;    values[12] = 0;
        values[1] = 0;    values[5] = 1;     values[9] = 0;    values[13] = 0;
        values[2] = 0;    values[6] = 0;    values[10] = 1;    values[14] = 0;
        values[3] = 0;    values[7] = 0;    values[11] = 0;    values[15] = 1;

        return this;
    }

    public Matrix4f modelMatrix(Vector3f _position, Vector3f _, Vector3f _scale)
    {
        this.identity().translate(_position);
        this.scale(_scale);
        return this;
    }

    private void translate(Vector3f _position)
    {
        values[12] = _position.x;
        values[13] = _position.y;
        values[14] = _position.z;

    }

    private void rotate(){}

    public void scale(Vector3f _scale)
    {
        values[0] = _scale.x;
        values[5] = _scale.y;
        values[10] = _scale.z;
    }


}
