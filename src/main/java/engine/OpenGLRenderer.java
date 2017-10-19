package engine;

import gameObject.GameObject;
import math.Matrix4;
import math.Vector3;
import math.Vector4;
import utils.Logger;
import utils.Utils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class OpenGLRenderer {

    private static final int NUMBER_OF_FRUSTUM_PLANES = 6;
    private final Window window;

    private final Matrix4 projectionMatrix;
    private final Matrix4 modelMatrix;
    private final Matrix4 viewMatrix;
    private final Matrix4 modelViewMatrix;

    private final Vector4[] frustumPlanes;
    private ShaderProgram shader;

    public OpenGLRenderer(Window _window)
    {
        this.window = _window;

        projectionMatrix = new Matrix4();
        modelMatrix = new Matrix4();
        viewMatrix = new Matrix4();
        modelViewMatrix = new Matrix4();

        frustumPlanes = new Vector4[6];
        for (int i = 0; i < 6; i++)
        {
            frustumPlanes[i] = new Vector4();
        }
    }

    public void init() throws Exception
    {
        float aspectRatio = (float)window.getWidth() / window.getHeight();
        projectionMatrix.setPerspective(EngineOptions.FOV, aspectRatio, EngineOptions.Z_NEAR, EngineOptions.Z_FAR);

        setupOpenGl();
        initShader();

        shader.bind();
        shader.setUniformData("projectionMatrix", projectionMatrix);
        shader.setUniformData("unicolorColor", EngineOptions.UNICOLOR_COLOR);
        shader.setUniformData("isShaded", EngineOptions.IS_SHADED);
        shader.setUniformData("showDepth", EngineOptions.SHOW_DEPTH);
        shader.setUniformData("isTextured", EngineOptions.IS_TEXTURED);

        if(EngineOptions.SHOW_WIREFRAME)
        {
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        }
        else
        {
            glPolygonMode(GL_FRONT_FACE, GL_FILL);
        }
    }

    public void render(GameObject[] _gameObjects, Vector3 _lightPosition)
    {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);

        if ( window.isResized() )
        {
            float aspectRatio = (float)window.getWidth() / window.getHeight();
            projectionMatrix.setPerspective(EngineOptions.FOV, aspectRatio, EngineOptions.Z_NEAR, EngineOptions.Z_FAR);
            shader.setUniformData("projectionMatrix", projectionMatrix);
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        //UPLOAD FRAME RELEVANT UNIFORMS HERE

        shader.setUniformData("lightPosition", _lightPosition);

        //FILTER OBJECTS FOR FRUSTUM CULLING
        if(EngineOptions.FRUSTUM_CULLING)
        {
            //UPDATE FRUSTUM PLANES TODO: don't update if you want to freeze the culling
            for(int i = 0; i < NUMBER_OF_FRUSTUM_PLANES; i++)
            {
                //TODO: find out which matrix to use here!
                //viewProjectionMatrix.calcFrustumPlane(i, frustumPlanes[i]);
            }

            for(GameObject temp : _gameObjects)
            {
                //IS OBJECT INSIDE FRUSTUM?
                Vector3 position = temp.getPosition();
                boolean isInsideFrustum = true;
                for (int i = 0; i < NUMBER_OF_FRUSTUM_PLANES; i++)
                {
                    Vector4 plane = frustumPlanes[i];
                    if (plane.x * position.x + plane.y * position.y + plane.z * position.z + plane.w <= -temp.getBoundingRadius() )
                    {
                        isInsideFrustum = false;
                    }
                }

                temp.setVisibility(isInsideFrustum);
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
                modelViewMatrix.multiply(modelMatrix, viewMatrix);
                shader.setUniformData("modelViewMatrix", modelViewMatrix);

                shader.setUniformData("texture_sampler", 0);
                glActiveTexture(GL_TEXTURE0);
                glBindTexture(GL_TEXTURE_2D, temp.getMaterial().getTexture().getID());

                if(EngineOptions.SHOW_WIREFRAME)
                {
                    glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);
                    glDrawElements(GL_POINTS, vertexCount, GL_UNSIGNED_INT, 0);
                }
                else
                {
                    glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);
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
