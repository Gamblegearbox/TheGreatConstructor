package input;

import org.lwjgl.glfw.GLFWKeyCallback;

import static org.lwjgl.glfw.GLFW.*;


public class KeyboardInput extends GLFWKeyCallback{

    private static final int[] keyStatus = new int[65536];

    @Override
    public void invoke(long _window, int _key, int _scancode, int _action, int _mods)
    {
        keyStatus[_key] = _action;
    }

    public static boolean isKeyRepeated(int _key)
    {
        return keyStatus[_key] == GLFW_PRESS || keyStatus[_key] == GLFW_REPEAT;
    }

    public static boolean isKeyPressedOnce(int _key)
    {
        boolean answer = keyStatus[_key] == GLFW_PRESS;
        if(answer){
            keyStatus[_key] = -1;
            return true;
        }else{
            return false;
        }
    }

    public static boolean isKeyReleased(int _key)
    {
        boolean answer = keyStatus[_key] == GLFW_RELEASE;
        if(answer){
            keyStatus[_key] = -1;
            return true;
        }else{
            return false;
        }
    }

    public static void init(){

        for(int i = 0; i < keyStatus.length; i++){
            keyStatus[i] = -1;
        }
    }

}
