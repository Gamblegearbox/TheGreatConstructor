package engine;


import math.Vector3f;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.GL_SHADING_LANGUAGE_VERSION;

public class EngineOptions {

    static final String OPERATING_SYSTEM = System.getProperty("os.name");
    static final String TITLE = "Television 2000";

    static final int WINDOW_WIDTH = 500;
    static final int WINDOW_HEIGHT = 500;

    static final float Z_NEAR = 0.05f;
    static final float Z_FAR = 1000f;
    static final float FOV = 60f;

    private static final int TARGET_FPS = 60;

    static final boolean DEBUG = true;

    static final boolean ANTIALIASING = true;
    static final boolean V_SYNC = false;

    static final boolean CULL_BACK_FACE = true ;
    static final boolean FRUSTUM_CULLING = true;

    static final float POINT_SIZE = 4f;
    static final Vector3f LINE_COLOR = new Vector3f(1.0f,1.0f,1.0f);
    static final Vector3f POINT_COLOR = new Vector3f(0.5f,0.5f,0.5f);
    static final Vector3f UNICOLOR_COLOR = new Vector3f(0.75f,0.75f,0.75f);

    static final renderMode RENDER_MODE = renderMode.SHADED_UNICOLOR;

    public enum renderMode
    {
        SHADED, SHADED_UNICOLOR, WIREFRAME_OVERLAY, WIREFRAME
    }

    static void printAllInfo()
    {
        printGeneralInfo();
        printResolutionAndTimerSettings();
        printOptionStatus();
    }

    private static void printGeneralInfo()
    {
        System.out.println();
        System.out.println("OPERATING SYSTEM:   " + OPERATING_SYSTEM);
        System.out.println("OPENGL VENDOR:      " + glGetString(GL_VENDOR));
        System.out.println("RENDERER:           " + glGetString(GL_RENDERER));
        System.out.println("OPENGL VERSION:     " + glGetString(GL_VERSION));
        System.out.println("GLSL VERSION:       " + glGetString(GL_SHADING_LANGUAGE_VERSION));
    }

    private static void printResolutionAndTimerSettings()
    {
        System.out.println();
        System.out.println("RESOLUTION:         " + WINDOW_WIDTH + " x " + WINDOW_HEIGHT);
        System.out.println("TARGET FPS:         " + TARGET_FPS);
    }

    private static void printOptionStatus()
    {
        System.out.println();
        System.out.println("RENDER MODE:        " + RENDER_MODE);
        System.out.println("DEBUG MODE:         " + convertBooleanToEnabledOrDisabled(DEBUG));
        System.out.println("ANTIALIASING:       " + convertBooleanToEnabledOrDisabled(ANTIALIASING));
        System.out.println("VSYNC:              " + convertBooleanToEnabledOrDisabled(V_SYNC));
        System.out.println("BACKFACE CULLING:   " + convertBooleanToEnabledOrDisabled(CULL_BACK_FACE));
        System.out.println();
    }

    private static String convertBooleanToEnabledOrDisabled(boolean status)
    {
        return status ? "Enabled" : "Disabled";
    }
}
