package math;

import org.joml.Quaternionf;
import utils.Logger;

public class Matrix4 {

    /*
    m[0];  m[4];  m[8];  m[12]
    m[1];  m[5];  m[9];  m[13]
    m[2];  m[6];  m[10]; m[14]
    m[3];  m[7];  m[11]; m[15]
    */

    private final static int PLANE_NX = 0;
    private final static int PLANE_PX = 1;
    private final static int PLANE_NY = 2;
    private final static int PLANE_PY = 3;
    private final static int PLANE_NZ = 4;
    private final static int PLANE_PZ = 5;
    private final float[] m;


    public Matrix4()
    {
        m = new float[16];

        m[0] = 1;  m[4] = 0;  m[8] = 0;  m[12] = 0;
        m[1] = 0;  m[5] = 1;  m[9] = 0;  m[13] = 0;
        m[2] = 0;  m[6] = 0;  m[10] = 1; m[14] = 0;
        m[3] = 0;  m[7] = 0;  m[11] = 0; m[15] = 1;
    }

    public float[] getValues()
    {
        return m;
    }

    public void setPerspective(float _fovInDegrees, float _aspectRatio, float _zNear, float _zFar)
    {
        float fov = (float) Math.toRadians(_fovInDegrees);

        float magicFov = 1.0f / (float)Math.tan(fov / 2.0f);
        float zm = _zFar - _zNear;
        float zp = _zFar + _zNear;

        m[0] = magicFov / _aspectRatio;
        m[5] = magicFov;
        m[10] = -zp / zm;
        m[11] = -1;
        m[14] = -(2 * _zFar * _zNear) / zm;
        m[15] = 0;
    }

    public void setIdentity()
    {
        m[0] = 1;
        m[5] = 1;
        m[10] = 1;
        m[15] = 1;
    }

    public void setModelValues(Vector3 _position, Quaternionf _rotation, Vector3 _scale)
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

         m[0] = (_scale.x - (q11 + q22) * _scale.x);
         m[1] = ((q01 + q23) * _scale.x);
         m[2] = ((q02 - q13) * _scale.x);
         //m[3] = (0.0f);
         m[4] = ((q01 - q23) * _scale.y);
         m[5] = (_scale.y - (q22 + q00) * _scale.y);
         m[6] = ((q12 + q03) * _scale.y);
         //m[7] = (0.0f);
         m[8] = ((q02 + q13) * _scale.z);
         m[9] = ((q12 - q03) * _scale.z);
        m[10] = (_scale.z - (q11 + q00) * _scale.z);
        //m[11] = (0.0f);
        m[12] = (_position.x);
        m[13] = (_position.y);
        m[14] = (_position.z);
        //m[15] = (1.0f);
    }

    public void multiply(Matrix4 _matrix1, Matrix4 _matrix2)
    {
        float[] m1 = _matrix1.getValues();
        float[] m2 = _matrix2.getValues();

         m[0] = m1[0] * m2[0]  + m1[4] * m2[1]  + m1[8]  * m2[2]  + m1[12] * m2[3];
         m[1] = m1[1] * m2[0]  + m1[5] * m2[1]  + m1[9]  * m2[2]  + m1[13] * m2[3];
         m[2] = m1[2] * m2[0]  + m1[6] * m2[1]  + m1[10] * m2[2]  + m1[14] * m2[3];
         m[3] = m1[3] * m2[0]  + m1[7] * m2[1]  + m1[11] * m2[2]  + m1[15] * m2[3];
         m[4] = m1[0] * m2[4]  + m1[4] * m2[5]  + m1[8]  * m2[6]  + m1[12] * m2[7];
         m[5] = m1[1] * m2[4]  + m1[5] * m2[5]  + m1[9]  * m2[6]  + m1[13] * m2[7];
         m[6] = m1[2] * m2[4]  + m1[6] * m2[5]  + m1[10] * m2[6]  + m1[14] * m2[7];
         m[7] = m1[3] * m2[4]  + m1[7] * m2[5]  + m1[11] * m2[6]  + m1[15] * m2[7];
         m[8] = m1[0] * m2[8]  + m1[4] * m2[9]  + m1[8]  * m2[10] + m1[12] * m2[11];
         m[9] = m1[1] * m2[8]  + m1[5] * m2[9]  + m1[9]  * m2[10] + m1[13] * m2[11];
        m[10] = m1[2] * m2[8]  + m1[6] * m2[9]  + m1[10] * m2[10] + m1[14] * m2[11];
        m[11] = m1[3] * m2[8]  + m1[7] * m2[9]  + m1[11] * m2[10] + m1[15] * m2[11];
        m[12] = m1[0] * m2[12] + m1[4] * m2[13] + m1[8]  * m2[14] + m1[12] * m2[15];
        m[13] = m1[1] * m2[12] + m1[5] * m2[13] + m1[9]  * m2[14] + m1[13] * m2[15];
        m[14] = m1[2] * m2[12] + m1[6] * m2[13] + m1[10] * m2[14] + m1[14] * m2[15];
        m[15] = m1[3] * m2[12] + m1[7] * m2[13] + m1[11] * m2[14] + m1[15] * m2[15];
    }

    //FROM JOML MATRIX4F
    public Vector4 calcFrustumPlane(int plane, Vector4 _planeEquation)
    {
        /*
        m00  m10  m20  m30  |   m[0];  m[4];  m[8];  m[12]
        m01  m11  m21  m31  |   m[1];  m[5];  m[9];  m[13]
        m02  m12  m22  m32  |   m[2];  m[6];  m[10]; m[14]
        m03  m13  m23  m33  |   m[3];  m[7];  m[11]; m[15]
        */

        switch (plane)
        {
            case PLANE_NX:
                //_planeEquation.set(m03 + m00, m13 + m10, m23 + m20, m33 + m30).normalize3();
                _planeEquation.set(m[3] + m[0], m[7] + m[4], m[11] + m[8], m[15] + m[12]);
                _planeEquation.normalize3();
                break;
            case PLANE_PX:
                //_planeEquation.set(m03 - m00, m13 - m10, m23 - m20, m33 - m30).normalize3();
                _planeEquation.set(m[12] - m[0], m[7] - m[4], m[11] - m[8], m[15] - m[12]);
                _planeEquation.normalize3();
                break;
            case PLANE_NY:
                //_planeEquation.set(m03 + m01, m13 + m11, m23 + m21, m33 + m31).normalize3();
                _planeEquation.set(m[12] + m[1], m[7] + m[5], m[11] + m[9], m[15] + m[13]);
                _planeEquation.normalize3();
                break;
            case PLANE_PY:
                //_planeEquation.set(m03 - m01, m13 - m11, m23 - m21, m33 - m31).normalize3();
                _planeEquation.set(m[12] - m[1], m[7] - m[5], m[11] - m[9], m[15] - m[13]);
                _planeEquation.normalize3();
                break;
            case PLANE_NZ:
                //_planeEquation.set(m03 + m02, m13 + m12, m23 + m22, m33 + m32).normalize3();
                _planeEquation.set(m[12] + m[2], m[7] + m[6], m[11] + m[10], m[15] + m[14]);
                _planeEquation.normalize3();
                break;
            case PLANE_PZ:
                //_planeEquation.set(m03 - m02, m13 - m12, m23 - m22, m33 - m32).normalize3();
                _planeEquation.set(m[12] - m[2], m[7] - m[6], m[11] - m[10], m[15] - m[14]);
                _planeEquation.normalize3();
                break;
            default:
                Logger.getInstance().writeTolog("wrong Plane number");
        }
        return _planeEquation;
    }
}
