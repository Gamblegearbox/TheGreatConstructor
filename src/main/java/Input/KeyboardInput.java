package input;

import org.lwjgl.glfw.GLFWKeyCallback;

import static org.lwjgl.glfw.GLFW.*;


public class KeyboardInput extends GLFWKeyCallback{

    private static int[] keyStatus = new int[65536];

    @Override
    public void invoke(long _window, int _key, int _scancode, int _action, int _mods)
    {
        keyStatus[_key] = _action;
    }

    public static boolean isKeyRepeated(int _key)
    {
        return keyStatus[_key] != GLFW_RELEASE;
    }

    public static boolean isKeyPressedOnce(int _key)
    {
        boolean answer = keyStatus[_key] == GLFW_PRESS;
        if(answer == true){
            keyStatus[_key] = 3;
            return true;
        }else{
            return false;
        }
    }

}
