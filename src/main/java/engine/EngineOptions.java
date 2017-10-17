package engine;

import math.Vector3;
import utils.Logger;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.GL_SHADING_LANGUAGE_VERSION;

public class EngineOptions {

    private static final int TRUE = 1;
    private static final int FALSE = 0;

    static final String OPERATING_SYSTEM = System.getProperty("os.name");
    static final String TITLE = "Television 2000";

    static final int WINDOW_WIDTH = 500;
    static final int WINDOW_HEIGHT = 500;

    static final float Z_NEAR = 0.05f;
    static final float Z_FAR = 1000f;
    static final float FOV = 60f;

    public static final boolean DEBUG = true;
    public static final boolean LOG_TO_FILE = false;
    public static final float LOGGING_INTERVAL = 1;

    static final multiSamplingMode MULTISAMPLING = multiSamplingMode.X4;
    static final boolean V_SYNC = false;
    static final boolean CULL_BACK_FACE = true ;
    static final boolean FRUSTUM_CULLING = true;

    static final float POINT_SIZE = 3f;
    static final Vector3 UNICOLOR_COLOR = new Vector3(0.75f,0.75f,0.75f);

    static final boolean SHOW_WIREFRAME = false;
    static final int IS_TEXTURED = TRUE;
    static final int IS_SHADED = TRUE;
    static final int SHOW_DEPTH = FALSE;

    public enum multiSamplingMode
    {
        OFF, X1, X2, X3, X4, X5, X6, X7, X8
    }

    static void logAllInfo()
    {
        logGeneralInfo();
        logOptionsStatus();
    }

    private static void logGeneralInfo()
    {
        String info =
        "OPERATING SYSTEM:   " + OPERATING_SYSTEM + "\n" +
        "OPENGL VENDOR:      " + glGetString(GL_VENDOR) + "\n" +
        "RENDERER:           " + glGetString(GL_RENDERER) + "\n" +
        "OPENGL VERSION:     " + glGetString(GL_VERSION) + "\n" +
        "GLSL VERSION:       " + glGetString(GL_SHADING_LANGUAGE_VERSION) + "\n" +
        "\n";

        Logger.getInstance().writeTolog(info);
    }

    static void logOptionsStatus()
    {
        String info =
        "DEBUG MODE:         " + convertBooleanToEnabledOrDisabled(DEBUG) + "\n" +
        "LOGGING INTERVAL    " + LOGGING_INTERVAL + "sec" + "\n" +
        "RESOLUTION:         " + WINDOW_WIDTH + " x " + WINDOW_HEIGHT + "\n" +
        "TEXTURES:           " + convertIntToEnabledOrDisabled(IS_TEXTURED) + "\n" +
        "SHADED GEOMETRY:    " + convertIntToEnabledOrDisabled(IS_SHADED) + "\n" +
        "SHOW DEPTH:         " + convertIntToEnabledOrDisabled(SHOW_DEPTH) + "\n" +
        "MULTISAMPLING:      " + MULTISAMPLING + "\n" +
        "VSYNC:              " + convertBooleanToEnabledOrDisabled(V_SYNC)  + "\n" +
        "BACKFACE CULLING:   " + convertBooleanToEnabledOrDisabled(CULL_BACK_FACE) + "\n" +
        "\n";

        Logger.getInstance().writeTolog(info);
    }

    private static String convertBooleanToEnabledOrDisabled(boolean _status)
    {
        return _status ? "Enabled" : "Disabled";
    }

    private static String convertIntToEnabledOrDisabled(int _status)
    {
        return _status > 0 ? "Enabled" : "Disabled";
    }
}
