package utils;

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
}
