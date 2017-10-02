package utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Utils {

    public static String loadResource(String fileName) throws Exception
    {
        String result;

        try (InputStream in = Utils.class.getClass().getResourceAsStream(fileName))
        {
            result = new Scanner(in, "UTF-8").useDelimiter("\\A").next();
        }

        return result;
    }

    public static List<String> readAllLines(String fileName) throws Exception
    {
        List<String> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(Utils.class.getClass().getResourceAsStream(fileName))))
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                list.add(line);
            }
        }

        return list;
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

}