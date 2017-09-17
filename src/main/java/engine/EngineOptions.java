package engine;


import math.Vector3f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glGetString;
import static org.lwjgl.opengl.GL20.GL_SHADING_LANGUAGE_VERSION;

public class EngineOptions {

    public static final String OPERATING_SYSTEM = System.getProperty("os.name");
    public static final String TITLE = "Television 2000";

    public static final int WINDOW_WIDTH = 500;
    public static final int WINDOW_HEIGHT = 500;
    public static final float ASPECT_RATIO = WINDOW_WIDTH / WINDOW_HEIGHT;

    public static final float Z_NEAR = 0.05f;
    public static final float Z_FAR = 1000f;
    public static final float FOV = 60f;

    public static final int TARGET_FPS = 60;

    public static final boolean DEBUG = true;

    public static final boolean ANTIALIASING = true;
    public static final boolean V_SYNC = true;

    public static final boolean CULL_BACK_FACE = true;

    public static final float POINT_SIZE = 5f;
    public static final Vector3f LINE_COLOR = new Vector3f(1.0f,1.0f,1.0f);
    public static final Vector3f POINT_COLOR = new Vector3f(0.0f,0.0f,0.0f);

    public static final renderMode RENDER_MODE = renderMode.WIREFRAME_OVERLAY;

    public enum renderMode
    {
        SHADED, WIREFRAME, WIREFRAME_OVERLAY, SHADED_UNICOLOR
    }

    public static void printAllInfo()
    {
        printGeneralInfo();
        printResolutionAndTimerSettings();
        printOptionStatus();
    }

    public static void printGeneralInfo()
    {
        System.out.println();
        System.out.println("OPERATING SYSTEM:   " + OPERATING_SYSTEM);
        System.out.println("OPENGL VENDOR:      " + glGetString(GL_VENDOR));
        System.out.println("RENDERER:           " + glGetString(GL_RENDERER));
        System.out.println("OPENGL VERSION:     " + glGetString(GL_VERSION));
        System.out.println("GLSL VERSION:       " + glGetString(GL_SHADING_LANGUAGE_VERSION));
    }

    public static void printResolutionAndTimerSettings()
    {
        System.out.println();
        System.out.println("RESOLUTION:         " + WINDOW_WIDTH + " x " + WINDOW_HEIGHT);
        System.out.println("TARGET FPS:         " + TARGET_FPS);
    }

    public static void printOptionStatus()
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
