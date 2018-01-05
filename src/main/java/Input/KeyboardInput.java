package Input;

import org.lwjgl.glfw.GLFWKeyCallback;

import static org.lwjgl.glfw.GLFW.*;

public class KeyboardInput extends GLFWKeyCallback{

    private static int[] keyStatus = new int[65536];

    @Override
    public void invoke(long _window, int _key, int _scancode, int _action, int _mods)
    {
        keyStatus[_key] = _action;
    }

    /* TODO: keys are always released when not pressed, how to detect a release once?
    public static boolean isKeyReleased(int _key)
    {
        return keyStatus[_key] == GLFW_RELEASE;
    }

    public static boolean isKeyRepeated(int _key)
    {
        return keyStatus[_key] == GLFW_REPEAT;
    }
    */

    public static boolean isKeyPressedOnce(int _key)//TODO: not working correctly
    {
        return keyStatus[_key] == GLFW_PRESS;
    }

    public static boolean isKeyDown(int _key)
    {
        return keyStatus[_key] != GLFW_RELEASE;
    }





}
