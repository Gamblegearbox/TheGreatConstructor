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

        shader.bind();
        shader.setUniformData("unicolorColor", EngineOptions.UNICOLOR_COLOR);
        shader.setUniformData("isShaded", EngineOptions.IS_SHADED);
        shader.setUniformData("showDepth", EngineOptions.SHOW_DEPTH);

        switch(EngineOptions.RENDER_MODE)
        {
            case WIREFRAME:
                shader.setUniformData("renderMode", 2);
                glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
                break;

            case UNICOLOR:
                shader.setUniformData("renderMode", 1);
                glPolygonMode(GL_FRONT_FACE, GL_FILL);
                break;

            case TEXTURED:
                shader.setUniformData("renderMode", 0);
                glPolygonMode(GL_FRONT_FACE, GL_FILL);
                break;
        }
    }

    public void render(GameObject[] _gameObjects, Vector3f _lightPosition)
    {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);

        if ( window.isResized() )
        {
            float aspectRatio = (float)window.getWidth() / window.getHeight();
            projectionMatrix.setPerspective(EngineOptions.FOV, aspectRatio, EngineOptions.Z_NEAR, EngineOptions.Z_FAR);
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        //UPLOAD FRAME RELEVANT UNIFORMS HERE
        viewProjectionMatrix.multiply(projectionMatrix, viewMatrix);
        shader.setUniformData("viewProjectionMatrix", viewProjectionMatrix);
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


        //RENDER ALL VISIBLE OBJECTS
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
                        shader.setUniformData("wireframeColor", EngineOptions.WIREFRAME_COLOR);
                        glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);
                        glDrawElements(GL_POINTS, vertexCount, GL_UNSIGNED_INT, 0);
                        break;

                    case UNICOLOR:

                        glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);
                        break;

                    case TEXTURED:

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
    }

}
