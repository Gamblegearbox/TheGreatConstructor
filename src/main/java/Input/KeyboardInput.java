package Input;

import org.lwjgl.glfw.GLFWKeyCallback;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyboardInput extends GLFWKeyCallback{

    public static boolean[] keys = new boolean[65536];

    @Override
    public void invoke(long _window, int _key, int _scancode, int _action, int _mods)
    {
        keys[_key] = _action != GLFW_RELEASE;
    }

    public static boolean isKeyDown(int _keycode)
    {
        return keys[_keycode];
    }
}
