package engine;


import gameObject.GameObject;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;


public class OpenGLRenderer {

    private final Window window;
    private ShaderProgram activeShader;

    public OpenGLRenderer(Window _window)
    {
        this.window = _window;
    }

    public void init() throws Exception
    {
        setupOpenGl();
        Shaders.initShaders();
        activeShader = Shaders.sceneShader;
        activeShader.bind();
    }

    public void prepareFrame()
    {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);

        if ( window.isResized() )
        {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

    }

    public void render(GameObject item)
    {
        OpenGLMesh mesh = item.getMesh();

        glBindVertexArray(mesh.getVaoID());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        switch(EngineOptions.RENDER_MODE)
        {
            case WIREFRAME:

                activeShader = Shaders.wireframeShader;
                activeShader.bind();
                glDrawElements(GL_LINE_LOOP, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);
                glDrawElements(GL_POINTS, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);

                break;
            case WIREFRAME_OVERLAY:

                activeShader = Shaders.sceneShader;
                activeShader.bind();
                glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);

                activeShader = Shaders.wireframeShader;
                activeShader.bind();

                glDrawElements(GL_LINE_LOOP, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);
                glDrawElements(GL_POINTS, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);

                break;
            case SHADED_UNICOLOR:

                activeShader = Shaders.sceneShader; // TODO: create an activate capmaterial shader
                activeShader.bind();
                glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);

                break;
            default:    //this is EngineOptions.rendermode.SHADED

                activeShader = Shaders.sceneShader;
                activeShader.bind();
                glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);

                break;
        }

        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
    }

    public void afterFrame()
    {
        activeShader.unbind();
    }

    public void cleanup()
    {
        if (activeShader != null)
        {
            activeShader.cleanup();
        }
    }

    private void setupOpenGl()
    {
        glClearColor(0.1f, 0.1f, 0.1f, 1.0f);

        glPointSize(EngineOptions.POINT_SIZE);

        glEnable(GL_DEPTH_TEST);
        glEnable(GL_STENCIL_TEST);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        switch(EngineOptions.RENDER_MODE)
        {
            case WIREFRAME:

                glDepthFunc(GL_LESS);
                glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

                break;
            case WIREFRAME_OVERLAY:

                glDepthFunc(GL_LEQUAL);
                glPolygonMode(GL_FRONT_FACE, GL_FILL);

                break;
            case SHADED_UNICOLOR:

                glDepthFunc(GL_LESS);
                glPolygonMode(GL_FRONT_FACE, GL_FILL);

                break;
            default:    //this is rendermode.SHADED

                glDepthFunc(GL_LESS);
                glPolygonMode(GL_FRONT_FACE, GL_FILL);

                break;
        }

        if(EngineOptions.CULL_BACK_FACE)
        {
            glEnable(GL_CULL_FACE);
            glCullFace(GL_BACK);
        }
        else
        {
            glDisable(GL_CULL_FACE);
        }

    }

}
