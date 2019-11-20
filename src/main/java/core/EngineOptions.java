package core;


public class EngineOptions {

    public static final boolean DEBUG_MODE = true;
    public static final boolean LOG_TO_FILE = false;
    public static final float LOGGING_INTERVAL = 1.0f;

    public static final String WINDOW_TITLE = "Television 2000";
    public static final int WINDOW_WIDTH = 1280;
    public static final int WINDOW_HEIGHT = 720;

    public static final int MULTISAMPLING = 0;
    public static final boolean V_SYNC = false;
    public static final boolean BACK_FACE_CULLING = true;
    public static final boolean FRUSTUM_CULLING = true;

    public static final float INITIAL_FOV = 30f;
    public static final float Z_NEAR = 0.5f;
    public static final float Z_FAR = 150f;

    public static final String OPERATING_SYSTEM = System.getProperty("os.name");


    public static void loadSettingFromFile(String _path) {
        //TODO: load if user changed settings
    }

    public static void saveSettingToFile()
    {
        //TODO: saving if setting were modified during game
    }

}
