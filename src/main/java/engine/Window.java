package engine;


import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private final String title;
    private int width;
    private int height;
    private long windowHandle;
    private boolean isResized;

    public Window(String title, int width, int height)
    {
        this.title = title;
        this.width = width;
        this.height = height;
        this.isResized = false;
    }

    public void init()
    {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err));

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit())
        {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE); // the window will be resizable
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

        // Antialiasing
        if (EngineOptions.ANTIALIASING) {
            glfwWindowHint(GLFW_SAMPLES, 4);
        }

        // Create the window
        windowHandle = glfwCreateWindow(width, height, title, NULL, NULL);
        if (windowHandle == NULL)
        {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        // Setup resize callback
        glfwSetWindowSizeCallback(windowHandle, new GLFWWindowSizeCallback()
        {
            @Override
            public void invoke(long window, int width, int height) {
                Window.this.width = width;
                Window.this.height = height;
                Window.this.setResized(true);
            }
        });

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        //TODO: glfwSetKeyCallback(windowHandle, new KeyboardInput());

        // Get the resolution of the primary monitor
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        // Center our window
        glfwSetWindowPos(
                windowHandle,
                (vidmode.width() - width) / 2,
                (vidmode.height() - height) / 2
        );

        // Make the OpenGL context current
        glfwMakeContextCurrent(windowHandle);

        if (EngineOptions.V_SYNC) {
            // Enable v-sync
            glfwSwapInterval(1);
        }
        else
        {
            glfwSwapInterval(0);
        }

        // Make the window visible
        glfwShowWindow(windowHandle);

        //TODO: Arrays.fill(KeyboardInput.keys, KeyboardInput.IDLE_STATE); // init all keys with idle state
        GL.createCapabilities();
    }

    public long getWindowHandle()
    {
        return windowHandle;
    }

    public boolean windowShouldClose()
    {
        return glfwWindowShouldClose(windowHandle);
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public boolean isResized()
    {
        return isResized;
    }

    public void setResized(boolean _isResized)
    {
        this.isResized = _isResized;
    }

    public void update()
    {
        glfwSwapBuffers(windowHandle);
        //TODO: KeyboardInput.update();
        glfwPollEvents();
    }

}
