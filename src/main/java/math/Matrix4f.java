package math;

import org.joml.Quaternionf;

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

    public void setPerspective(float _fovInDegrees, float _aspectRatio, float _zNear, float _zFar)
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
    }

    public void setIdentity()
    {
        values[0] = 1;    values[4] = 0;     values[8] = 0;    values[12] = 0;
        values[1] = 0;    values[5] = 1;     values[9] = 0;    values[13] = 0;
        values[2] = 0;    values[6] = 0;    values[10] = 1;    values[14] = 0;
        values[3] = 0;    values[7] = 0;    values[11] = 0;    values[15] = 1;
    }

    public void setModelValues(Vector3f _position, Quaternionf _rotation, Vector3f _scale)
    {
        float dqx = _rotation.x + _rotation.x;
        float dqy = _rotation.y + _rotation.y;
        float dqz = _rotation.z + _rotation.z;
        float q00 = dqx * _rotation.x;
        float q11 = dqy * _rotation.y;
        float q22 = dqz * _rotation.z;
        float q01 = dqx * _rotation.y;
        float q02 = dqx * _rotation.z;
        float q03 = dqx * _rotation.w;
        float q12 = dqy * _rotation.z;
        float q13 = dqy * _rotation.w;
        float q23 = dqz * _rotation.w;

         values[0] = (_scale.x - (q11 + q22) * _scale.x);
         values[1] = ((q01 + q23) * _scale.x);
         values[2] = ((q02 - q13) * _scale.x);
         values[3] = (0.0f);
         values[4] = ((q01 - q23) * _scale.y);
         values[5] = (_scale.y - (q22 + q00) * _scale.y);
         values[6] = ((q12 + q03) * _scale.y);
         values[7] = (0.0f);
         values[8] = ((q02 + q13) * _scale.z);
         values[9] = ((q12 - q03) * _scale.z);
        values[10] = (_scale.z - (q11 + q00) * _scale.z);
        values[11] = (0.0f);
        values[12] = (_position.x);
        values[13] = (_position.y);
        values[14] = (_position.z);
        values[15] = (1.0f);
    }

}
