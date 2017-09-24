package utils;

import org.joml.Math;
import org.joml.Quaternionf;

public class Conversions {

    public static float convertMPStoKMH(float metersPerSecond)
    {
        return metersPerSecond * 3.6f;
    }

    //Waterflames, http://stackoverflow.com/questions/12088610/conversion-between-euler-quaternion-like-in-unity3d-engine, 15.02.2015
    public static void convertEulerToQuaternion(float _xRot, float _yRot, float _zRot, Quaternionf _quaternion)
    {
        _xRot = (float) Math.toRadians(_xRot);
        _yRot = (float)Math.toRadians(_yRot);
        _zRot = (float)Math.toRadians(_zRot);

        double yawOver2 = _xRot * 0.5f;
        float cosYawOver2 = (float)Math.cos(yawOver2);
        float sinYawOver2 = (float)Math.sin(yawOver2);
        double pitchOver2 = _yRot * 0.5f;
        float cosPitchOver2 = (float)Math.cos(pitchOver2);
        float sinPitchOver2 = (float)Math.sin(pitchOver2);
        double rollOver2 = _zRot * 0.5f;
        float cosRollOver2 = (float)Math.cos(rollOver2);
        float sinRollOver2 = (float)Math.sin(rollOver2);

        _quaternion.w = cosYawOver2 * cosPitchOver2 * cosRollOver2 + sinYawOver2 * sinPitchOver2 * sinRollOver2;
        _quaternion.x = sinYawOver2 * cosPitchOver2 * cosRollOver2 + cosYawOver2 * sinPitchOver2 * sinRollOver2;
        _quaternion.y = cosYawOver2 * sinPitchOver2 * cosRollOver2 - sinYawOver2 * cosPitchOver2 * sinRollOver2;
        _quaternion.z = cosYawOver2 * cosPitchOver2 * sinRollOver2 - sinYawOver2 * sinPitchOver2 * cosRollOver2;
    }
}
