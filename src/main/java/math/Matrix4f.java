package math;

import org.joml.Quaternionf;

public class Matrix4f {

    private final float[] values;

    public Matrix4f()
    {
        values = new float[16];

        values[0] = 1;  values[4] = 0;  values[8] = 0;  values[12] = 0;
        values[1] = 0;  values[5] = 1;  values[9] = 0;  values[13] = 0;
        values[2] = 0;  values[6] = 0;  values[10] = 1; values[14] = 0;
        values[3] = 0;  values[7] = 0;  values[11] = 0; values[15] = 1;
    }

    public float[] getValues()
    {
        return values;
    }

    public void setPerspective(float _fovInDegrees, float _aspectRatio, float _zNear, float _zFar)
    {
        float fov = (float) Math.toRadians(_fovInDegrees);

        float magicFov = 1.0f / (float)Math.tan(fov / 2.0f);
        float zm = _zFar - _zNear;
        float zp = _zFar + _zNear;

        values[0] = magicFov / _aspectRatio;
        values[5] = magicFov;
        values[10] = -zp / zm;
        values[11] = -1;
        values[14] = -(2 * _zFar * _zNear) / zm;
        values[15] = 0;
    }

    public void setIdentity()
    {
        values[0] = 1;
        values[5] = 1;
        values[10] = 1;
        values[15] = 1;
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
         //values[3] = (0.0f);
         values[4] = ((q01 - q23) * _scale.y);
         values[5] = (_scale.y - (q22 + q00) * _scale.y);
         values[6] = ((q12 + q03) * _scale.y);
         //values[7] = (0.0f);
         values[8] = ((q02 + q13) * _scale.z);
         values[9] = ((q12 - q03) * _scale.z);
        values[10] = (_scale.z - (q11 + q00) * _scale.z);
        //values[11] = (0.0f);
        values[12] = (_position.x);
        values[13] = (_position.y);
        values[14] = (_position.z);
        //values[15] = (1.0f);
    }

    public void multiply(Matrix4f _matrix1, Matrix4f _matrix2)
    {
        /*
        values[0];  values[4];  values[8];  values[12]
        values[1];  values[5];  values[9];  values[13]
        values[2];  values[6];  values[10]; values[14]
        values[3];  values[7];  values[11]; values[15]
        */

        float[] m1 = _matrix1.getValues();
        float[] m2 = _matrix2.getValues();

         values[0] = m1[0] * m2[0]  + m1[4] * m2[1]  + m1[8]  * m2[2]  + m1[12] * m2[3];
         values[1] = m1[1] * m2[0]  + m1[5] * m2[1]  + m1[9]  * m2[2]  + m1[13] * m2[3];
         values[2] = m1[2] * m2[0]  + m1[6] * m2[1]  + m1[10] * m2[2]  + m1[14] * m2[3];
         values[3] = m1[3] * m2[0]  + m1[7] * m2[1]  + m1[11] * m2[2]  + m1[15] * m2[3];
         values[4] = m1[0] * m2[4]  + m1[4] * m2[5]  + m1[8]  * m2[6]  + m1[12] * m2[7];
         values[5] = m1[1] * m2[4]  + m1[5] * m2[5]  + m1[9]  * m2[6]  + m1[13] * m2[7];
         values[6] = m1[2] * m2[4]  + m1[6] * m2[5]  + m1[10] * m2[6]  + m1[14] * m2[7];
         values[7] = m1[3] * m2[4]  + m1[7] * m2[5]  + m1[11] * m2[6]  + m1[15] * m2[7];
         values[8] = m1[0] * m2[8]  + m1[4] * m2[9]  + m1[8]  * m2[10] + m1[12] * m2[11];
         values[9] = m1[1] * m2[8]  + m1[5] * m2[9]  + m1[9]  * m2[10] + m1[13] * m2[11];
        values[10] = m1[2] * m2[8]  + m1[6] * m2[9]  + m1[10] * m2[10] + m1[14] * m2[11];
        values[11] = m1[3] * m2[8]  + m1[7] * m2[9]  + m1[11] * m2[10] + m1[15] * m2[11];
        values[12] = m1[0] * m2[12] + m1[4] * m2[13] + m1[8]  * m2[14] + m1[12] * m2[15];
        values[13] = m1[1] * m2[12] + m1[5] * m2[13] + m1[9]  * m2[14] + m1[13] * m2[15];
        values[14] = m1[2] * m2[12] + m1[6] * m2[13] + m1[10] * m2[14] + m1[14] * m2[15];
        values[15] = m1[3] * m2[12] + m1[7] * m2[13] + m1[11] * m2[14] + m1[15] * m2[15];
    }

}
