package engine.input;

import org.lwjgl.glfw.GLFWKeyCallback;

import static org.lwjgl.glfw.GLFW.*;

public class KeyboardInput extends GLFWKeyCallback {

    public static int[] keys = new int[500];
    public static final int IDLE_STATE = -1;

    // The GLFWKeyCallback class is an abstract method that
    // can't be instantiated by itself and must instead be extended
    @Override
    public void invoke(long window, int key, int scancode, int action, int mods)
    {
        keys[key] = action;
    }

    public static boolean isKeyPressed(int keycode)
    {
        return keys[keycode] == GLFW_PRESS || keys[keycode] == GLFW_REPEAT;
    }

    public static boolean isKeyReleased(int keycode)
    {
        return keys[keycode] == GLFW_RELEASE;
    }

    public static void update()
    {
        for(int i = 0; i < keys.length; i++)
        {
            if(keys[i] == GLFW_RELEASE)
            {
                keys[i] = IDLE_STATE;
            }
        }
    }

}
