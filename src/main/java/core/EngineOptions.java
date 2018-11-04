package core;

import math.Vector3;
import utils.Logger;
import utils.Utils;

import java.util.HashMap;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.GL_SHADING_LANGUAGE_VERSION;

public class EngineOptions {

    static final String WINDOW_TITLE = "Television 2000";
    private static final HashMap<String, Float> options = new HashMap<>();
    private static List<String> configFileContent;
    public static final String OPERATING_SYSTEM = System.getProperty("os.name");

    public static boolean getOptionAsBoolean(String _option)
    {
        boolean value;
        if(options.containsKey(_option))
        {
            value = options.get(_option) > 0;
        }
        else
        {
            Logger.getInstance().writeln("Could not find option: " + _option);
            value = false;
        }

        return value;
    }

    public static int getOptionAsInt(String _option)
    {
        int value;
        if(options.containsKey(_option))
        {
            value = options.get(_option).intValue();
        }
        else
        {
            Logger.getInstance().writeln("Could not find option: " + _option);
            value = 0xdead;
        }

        return value;
    }

    public static float getOptionAsFloat(String _option)
    {
        float value;
        if(options.containsKey(_option))
        {
            value = options.get(_option);
        }
        else
        {
            Logger.getInstance().writeln("Could not find option: " + _option);
            value = 0xdead;
        }

        return value;
    }

    public static void logOptionsStatus()
    {
        String info = "";
        for(String line : configFileContent)
        {
            info += "\t" + line + "\n";
        }
        info += "\n";

        Logger.getInstance().write(info);
    }

    public static void loadSettingFromFile(String _path) throws Exception
    {
        configFileContent = Utils.readAllLines(_path);

        for(String line : configFileContent)
        {
            if(line.startsWith("[") || line.isEmpty())
            {
                continue;
            }
            else
            {
                line = line.replace(" ", "");
                line = line.replace(";", "");

                String[] keyValue = line.split("=");
                options.put(keyValue[0], Float.parseFloat(keyValue[1]));
            }
        }
    }

    public static void saveSettingToFile()
    {
        //TODO: saving if setting were modified during game
    }

}
