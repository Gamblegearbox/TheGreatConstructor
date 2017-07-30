package engine.input;

import org.lwjgl.glfw.GLFWKeyCallback;

import static org.lwjgl.glfw.GLFW.*;

public class KeyboardInput extends GLFWKeyCallback {

    public static int[] keys = new int[65536];
    public static final int IDLE_STATE = -1;
    // The GLFWKeyCallback class is an abstract method that
    // can't be instantiated by itself and must instead be extended
    //
    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        // TODO Auto-generated method stub
        keys[key] = action;
    }

    // boolean method that returns true if a given key
    // is pressed.
    public static boolean isKeyDown(int keycode)
    {
        return keys[keycode] == GLFW_PRESS;
    }

    public static boolean isKeyReleased(int keycode)
    {
        return keys[keycode] == GLFW_RELEASE;
    }

    public static boolean isKeyPressed(int keycode)
    {
        return keys[keycode] == GLFW_REPEAT;
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
