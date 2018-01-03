package engine;

import math.Vector3;
import utils.Logger;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.GL_SHADING_LANGUAGE_VERSION;

public class EngineOptions {

    static final String WINDOW_TITLE = "Television 2000";

    static final String OPERATING_SYSTEM = System.getProperty("os.name");
    private static final String OPERATING_SYSTEM_VERSION = System.getProperty("os.version");
    private static final String OPERATING_SYSTEM_ARCHITECTURE = System.getProperty("os.arch");

    static final int WINDOW_WIDTH = 1000;
    static final int WINDOW_HEIGHT = 700;
    static final multiSamplingMode MULTISAMPLING = multiSamplingMode.X4;
    static final boolean V_SYNC = false;
    static final boolean BACK_FACE_CULLING = true ;
    static final boolean FRUSTUM_CULLING = true;

    static final float Z_NEAR = 0.05f;
    static final float Z_FAR = 1000f;
    static final float FOV = 60f;

    public static final boolean DEBUG_MODE = true;
    public static final boolean LOG_TO_FILE = false;
    public static final float LOGGING_INTERVAL = 1;

    static final float POINT_SIZE = 3f;
    static final Vector3 UNICOLOR_COLOR = new Vector3(0.75f,0.75f,0.75f);
    static final float UNICOLOR_OPACITY = 0.5f;

    static final boolean IS_SHADED = true;
    static final boolean SHOW_DEPTH = false;
    static final boolean SHOW_WIREFRAME = false;
    static final boolean ENABLE_NORMALS_TO_COLOR = false;

    static final boolean ENABLE_DIFFUSE_MAPPING = true;
    static final boolean ENABLE_NORMAL_MAPPING = true;
    static final boolean ENABLE_GLOSS_MAPPING = true;
    static final boolean ENABLE_ILLUMINATION_MAPPING = true;


    public enum multiSamplingMode
    {
        OFF, X1, X2, X3, X4, X5, X6, X7, X8
    }

    static void logAllInfo()
    {
        logSystemInfo();
        logOptionsStatus();
    }

    private static void logSystemInfo()
    {
        String info = "\n" +
        "OPERATING SYSTEM:      " + OPERATING_SYSTEM + "\n" +
        "VERSION:               " + OPERATING_SYSTEM_VERSION + "\n" +
        "ARCHITECTURE:          " + OPERATING_SYSTEM_ARCHITECTURE + "\n" +
        "\n" +
        //The following throws ACCESS_VIOLATION_EXCEPTIONs if called to early (so no static final variable ;))
        "OPENGL VENDOR:         " + glGetString(GL_VENDOR) + "\n" +
        "RENDERER:              " + glGetString(GL_RENDERER) + "\n" +
        "OPENGL VERSION:        " + glGetString(GL_VERSION) + "\n" +
        "GLSL VERSION:          " + glGetString(GL_SHADING_LANGUAGE_VERSION) + "\n" +
        "\n";

        Logger.getInstance().write(info);
    }

    static void logOptionsStatus()
    {
        String info =
        "DEBUG MODE:            " + convertBooleanToEnabledOrDisabled(DEBUG_MODE) + "\n" +
        "LOGGING INTERVAL       " + LOGGING_INTERVAL + "sec" + "\n" +
        "\n" +
        "RESOLUTION:            " + WINDOW_WIDTH + " x " + WINDOW_HEIGHT + "\n" +
        "MULTISAMPLING:         " + MULTISAMPLING + "\n" +
        "VSYNC:                 " + convertBooleanToEnabledOrDisabled(V_SYNC)  + "\n" +
        "BACKFACE CULLING:      " + convertBooleanToEnabledOrDisabled(BACK_FACE_CULLING) + "\n" +
        "FRUSTUM CULLING:       " + convertBooleanToEnabledOrDisabled(FRUSTUM_CULLING) + "\n" +
        "\n" +
        "SHADED GEOMETRY:       " + convertBooleanToEnabledOrDisabled(IS_SHADED) + "\n" +
        "SHOW DEPTH:            " + convertBooleanToEnabledOrDisabled(SHOW_DEPTH) + "\n" +
        "SHOW WIREFRAME:        " + convertBooleanToEnabledOrDisabled(SHOW_DEPTH) + "\n" +
        "NORMALS TO COLOR:      " + convertBooleanToEnabledOrDisabled(ENABLE_NORMALS_TO_COLOR) + "\n" +
        "\n" +
        "USE DIFFUSE MAPS:      " + convertBooleanToEnabledOrDisabled(ENABLE_DIFFUSE_MAPPING) + "\n" +
        "USE NORMAL MAPS:       " + convertBooleanToEnabledOrDisabled(ENABLE_NORMAL_MAPPING) + "\n" +
        "USE SPECULAR MAPS:     " + convertBooleanToEnabledOrDisabled(ENABLE_GLOSS_MAPPING) + "\n" +
        "USE ILLUMINATION MAPS: " + convertBooleanToEnabledOrDisabled(ENABLE_ILLUMINATION_MAPPING) + "\n" +
        "\n";

        Logger.getInstance().write(info);
    }

    private static String convertBooleanToEnabledOrDisabled(boolean _status)
    {
        return _status ? "Enabled" : "Disabled";
    }

}
