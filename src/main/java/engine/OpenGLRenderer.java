package engine;

import gameObject.GameObject;
import math.Matrix4f;
import utils.Utils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class OpenGLRenderer {

    private final Window window;
    private final Matrix4f projectionMatrix;

    private ShaderProgram shader;
    private final Matrix4f modelMatrix;

    public OpenGLRenderer(Window _window)
    {
        this.window = _window;
        projectionMatrix = new Matrix4f();
        modelMatrix = new Matrix4f();
    }

    public void init() throws Exception
    {
        setupOpenGl();
        initMatrices();
        initShader();
    }

    public void prepareFrame()
    {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);

        shader.bind();

        if ( window.isResized() )
        {
            float aspectRatio = (float)window.getWidth() / window.getHeight();
            projectionMatrix.setPerspective(EngineOptions.FOV, aspectRatio, EngineOptions.Z_NEAR, EngineOptions.Z_FAR);
            shader.setUniformData("projectionMatrix", projectionMatrix);

            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }
    }

    public void render(GameObject object)
    {
        OpenGLMesh mesh = object.getMesh();

        glBindVertexArray(mesh.getVaoID());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        modelMatrix.setModelValues(object.getPosition(), object.getRotation(), object.getScale());
        shader.setUniformData("modelMatrix", modelMatrix);

        switch(EngineOptions.RENDER_MODE)
        {
            case WIREFRAME:
                shader.setUniformData("renderMode", 2);

                shader.setUniformData("wireframeColor", EngineOptions.LINE_COLOR);
                glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);

                shader.setUniformData("wireframeColor", EngineOptions.POINT_COLOR);
                glDrawElements(GL_POINTS, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);
                break;

            case WIREFRAME_OVERLAY:
                shader.setUniformData("renderMode", 0);
                glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);

                shader.setUniformData("renderMode", 2);
                shader.setUniformData("wireframeColor", EngineOptions.LINE_COLOR);
                glDrawElements(GL_LINE_STRIP, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);

                shader.setUniformData("wireframeColor", EngineOptions.POINT_COLOR);
                glDrawElements(GL_POINTS, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);
                break;

            case SHADED_UNICOLOR:
                shader.setUniformData("renderMode", 1);
                glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);
                break;

            case SHADED:

                shader.setUniformData("renderMode", 0);
                glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);
                break;
        }

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);
    }

    public void afterFrame()
    {
        shader.unbind();
    }

    public void cleanup()
    {
        if (shader != null)
        {
            shader.cleanup();
        }
    }

    private void setupOpenGl()
    {
        glClearColor(0.1f, 0.1f, 0.1f, 1.0f);

        glPointSize(EngineOptions.POINT_SIZE);

        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LESS);

        glEnable(GL_STENCIL_TEST);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        switch(EngineOptions.RENDER_MODE)
        {
            case WIREFRAME:
                glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
                break;

            case WIREFRAME_OVERLAY:
                glPolygonMode(GL_FRONT_FACE, GL_FILL);
                break;

            case SHADED_UNICOLOR:
                glPolygonMode(GL_FRONT_FACE, GL_FILL);
                break;

            case SHADED:
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

    private void initShader() throws Exception
    {
        shader = new ShaderProgram();
        shader.createVertexShader(Utils.loadResource("/shaders/shaded.vs"));
        shader.createFragmentShader (Utils.loadResource("/shaders/shaded.fs"));
        shader.link();

        shader.bind();
        shader.setUniformData("projectionMatrix", projectionMatrix);
        shader.setUniformData("unicolorColor", EngineOptions.UNICOLOR_COLOR);
    }

    private void initMatrices()
    {
        float aspectRatio = (float)window.getWidth() / window.getHeight();
        projectionMatrix.setPerspective(EngineOptions.FOV, aspectRatio, EngineOptions.Z_NEAR, EngineOptions.Z_FAR);
    }
}
