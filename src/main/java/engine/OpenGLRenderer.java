package engine;

import gameObject.GameObject;
import math.Matrix4f;
import math.Vector3f;
import utils.Logger;
import utils.Utils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class OpenGLRenderer {

    private final Window window;
    private final Matrix4f projectionMatrix;
    private final Matrix4f viewMatrix;
    private final Matrix4f viewProjectionMatrix;

    private ShaderProgram shader;
    private final Matrix4f modelMatrix;

    public OpenGLRenderer(Window _window)
    {
        this.window = _window;
        projectionMatrix = new Matrix4f();
        viewMatrix = new Matrix4f();
        viewProjectionMatrix = new Matrix4f();
        viewProjectionMatrix.multiply(projectionMatrix, viewMatrix);
        modelMatrix = new Matrix4f();
    }

    public void init() throws Exception
    {
        float aspectRatio = (float)window.getWidth() / window.getHeight();
        projectionMatrix.setPerspective(EngineOptions.FOV, aspectRatio, EngineOptions.Z_NEAR, EngineOptions.Z_FAR);

        setupOpenGl();
        initShader();
        initRenderMode();
    }

    public void render(GameObject[] _gameObjects, Vector3f _lightPosition)
    {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);

        //UPLOAD FRAME RELEVANT UNIFORMS HERE
        //shader.bind();
        shader.setUniformData("lightPosition", _lightPosition);

        //FILTER OBJECTS FOR FRUSTUM CULLING
        if(EngineOptions.FRUSTUM_CULLING)
        {
            for(GameObject temp : _gameObjects)
            {
                //TODO: filter objects
                if(temp.getPosition().x > 0)
                {
                    //temp.setVisibility(false);
                }
            }
        }

        if ( window.isResized() )
        {
            float aspectRatio = (float)window.getWidth() / window.getHeight();
            projectionMatrix.setPerspective(EngineOptions.FOV, aspectRatio, EngineOptions.Z_NEAR, EngineOptions.Z_FAR);
            viewProjectionMatrix.multiply(projectionMatrix, viewMatrix);
            shader.setUniformData("viewProjectionMatrix", viewProjectionMatrix);

            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        int totalVerticesInFrame = 0;

        for(GameObject temp : _gameObjects)
        {
            if(temp.isVisible())
            {
                OpenGLMesh mesh = temp.getMesh();
                int vertexCount = mesh.getVertexCount();
                totalVerticesInFrame += vertexCount;

                glBindVertexArray(mesh.getVaoID());
                glEnableVertexAttribArray(0);
                glEnableVertexAttribArray(1);
                glEnableVertexAttribArray(2);

                modelMatrix.setModelValues(temp.getPosition(), temp.getRotation(), temp.getScale());
                shader.setUniformData("modelMatrix", modelMatrix);

                switch(EngineOptions.RENDER_MODE)
                {
                    case WIREFRAME:
                        shader.setUniformData("wireframeColor", EngineOptions.LINE_COLOR);
                        glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);

                        shader.setUniformData("wireframeColor", EngineOptions.POINT_COLOR);
                        glDrawElements(GL_POINTS, vertexCount, GL_UNSIGNED_INT, 0);
                        break;

                    case WIREFRAME_OVERLAY:
                        shader.setUniformData("renderMode", 0); //TODO: unless it is not switchable during runtime put that in an init method
                        glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);

                        shader.setUniformData("renderMode", 2);
                        shader.setUniformData("wireframeColor", EngineOptions.LINE_COLOR);
                        glDrawElements(GL_LINE_STRIP, vertexCount, GL_UNSIGNED_INT, 0);

                        shader.setUniformData("wireframeColor", EngineOptions.POINT_COLOR);
                        glDrawElements(GL_POINTS, vertexCount, GL_UNSIGNED_INT, 0);
                        break;

                    case SHADED_UNICOLOR:

                        glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);
                        break;

                    case SHADED:

                        glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);
                        break;
                }

                glDisableVertexAttribArray(0);
                glDisableVertexAttribArray(1);
                glDisableVertexAttribArray(2);
                glBindVertexArray(0);
            }
        }

        if(EngineOptions.DEBUG)
        {
            Logger.getInstance().logData("VERTEX COUNT", totalVerticesInFrame);
        }
        //shader.unbind();
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
        shader.setUniformData("viewProjectionMatrix", viewProjectionMatrix);
        shader.setUniformData("unicolorColor", EngineOptions.UNICOLOR_COLOR);
    }

    private void initRenderMode()
    {
        switch(EngineOptions.RENDER_MODE)
        {
            case WIREFRAME:
                shader.setUniformData("renderMode", 2);
                glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
                break;

            case WIREFRAME_OVERLAY:

                glPolygonMode(GL_FRONT_FACE, GL_FILL);
                break;

            case SHADED_UNICOLOR:
                shader.setUniformData("renderMode", 1);
                glPolygonMode(GL_FRONT_FACE, GL_FILL);
                break;

            case SHADED:
                shader.setUniformData("renderMode", 0);
                glPolygonMode(GL_FRONT_FACE, GL_FILL);
                break;
        }
    }

}
