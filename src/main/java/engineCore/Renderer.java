package engineCore;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_BACK;

public class Renderer {

    public Renderer()
    {

    }

    public void init()
    {
        setupOpenGl();
    }

    public void render()
    {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
    }

    private void setupOpenGl()
    {
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_STENCIL_TEST);
        glEnable(GL_BLEND);

        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glPointSize(EngineOptions.POINT_SIZE);

        if(EngineOptions.WIREFRAME_MODE)
        {
            glClearColor(0.19f, 0.74f, 1.0f, 1.0f);
            glDepthFunc(GL_LEQUAL);
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
            glDisable(GL_CULL_FACE);
        }
        else
        {
            glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
            glDepthFunc(GL_LESS);
            glPolygonMode(GL_FRONT_FACE, GL_FILL);
            glEnable(GL_CULL_FACE);
            glCullFace(GL_BACK);
        }
    }

    public void cleanup()
    {

    }
}
