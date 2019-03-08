package utils;

import org.joml.Math;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class Utils {

    public static String loadResource(String fileName)
    {
        File path = new File(fileName);
        String result = null;
        try {
            result = new String(Files.readAllBytes(path.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static List<String> readAllLines(String _path) throws Exception
    {
        File file = new File(_path);
        List<String> result;

        result = Files.readAllLines(file.toPath());
        return result;
    }

    public static float[] listToArray(List<Float> list)
    {
        int size = list != null ? list.size() : 0;
        float[] floatArray = new float[size];

        for(int i = 0; i < size; i++)
        {
            floatArray[i] = list.get(i);
        }

        return floatArray;
    }

    public static Vector3f createPositionFromString(String _string)
    {
        String[] positionAsString = _string.split(",");
        float x = Float.parseFloat(positionAsString[0]);
        float y = Float.parseFloat(positionAsString[1]);
        float z = Float.parseFloat(positionAsString[2]);

        return new Vector3f(x, y, z);
    }

    public static String convertNormalizedFloatToTime(float _value){
        float timaeValue = _value * 24f;
        int hours = (int)timaeValue;
        float minutes = (timaeValue - hours) * 60;
        float seconds = (minutes - (int)minutes) * 60;

        return addLeadingZero(hours) + ":"
                + addLeadingZero((int)minutes) + ":"
                + addLeadingZero((int)seconds);
    }

    public static String addLeadingZero(int _value){
        String result = "" + _value;

        if(_value < 10){
            result = "0" + _value;
        }

        return result;
    }

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
