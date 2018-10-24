package rendering;

import core.*;
import interfaces.IF_SceneObject;
import math.Matrix4;
import math.Vector3;
import math.Vector4;
import utils.Logger;
import utils.Utils;

import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.GL_SHADING_LANGUAGE_VERSION;
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
    private final Matrix4 viewProjectionMatrix;

    private final Vector4[] frustumPlanes;
    private ShaderProgram shader;

    public OpenGLRenderer(Window _window)
    {
        this.window = _window;

        projectionMatrix = new Matrix4();
        modelMatrix = new Matrix4();
        viewMatrix = new Matrix4();
        modelViewMatrix = new Matrix4();
        viewProjectionMatrix = new Matrix4();

        frustumPlanes = new Vector4[6];
        for (int i = 0; i < 6; i++)
        {
            frustumPlanes[i] = new Vector4();
        }
    }

    public void init() throws Exception
    {
        Logger.getInstance().writeln(">> INITIALISING RENDERER");
        logGpuInfo();

        float aspectRatio = (float)window.getWidth() / window.getHeight();
        projectionMatrix.setPerspective(EngineOptions.getOptionAsFloat("FOV"), aspectRatio, EngineOptions.getOptionAsFloat("Z_NEAR"), EngineOptions.getOptionAsFloat("Z_FAR"));

        setupOpenGl();
        initShader();

        shader.bind();
        shader.setUniformData("projectionMatrix", projectionMatrix);
        shader.setUniformData("unicolorColor", EngineOptions.UNICOLOR_COLOR);
        shader.setUniformData("unicolorOpacity", EngineOptions.getOptionAsFloat("UNICOLOR_OPACITY"));

        shader.setUniformData("isShaded", EngineOptions.getOptionAsInt("IS_SHADED"));
        shader.setUniformData("showDepth", EngineOptions.getOptionAsInt("SHOW_DEPTH"));
        shader.setUniformData("enableNormalsToColor", EngineOptions.getOptionAsInt("ENABLE_NORMALS_TO_COLOR"));

        shader.setUniformData("diffuseMap_sampler", Texture.DIFFUSE);
        shader.setUniformData("normalMap_sampler", Texture.NORMALS);
        shader.setUniformData("glossMap_sampler", Texture.GLOSS);
        shader.setUniformData("illuminationMap_sampler", Texture.ILLUMINATION);

        if(EngineOptions.getOptionAsBoolean("SHOW_WIREFRAME"))
        {
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        }
        else
        {
            glPolygonMode(GL_FRONT_FACE, GL_FILL);
        }
    }

    public void logGpuInfo()
    {
        String info =
                "\tOPENGL VENDOR:             " + glGetString(GL_VENDOR) + "\n" +
                "\tRENDERER:                  " + glGetString(GL_RENDERER) + "\n" +
                "\tOPENGL VERSION:            " + glGetString(GL_VERSION) + "\n" +
                "\tGLSL VERSION:              " + glGetString(GL_SHADING_LANGUAGE_VERSION) + "\n" +
                "\n";

        Logger.getInstance().write(info);
    }

    public void render(Scene _scene)
    {
        int totalVerticesInFrame = 0;
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);

        if ( window.isResized() )
        {
            float aspectRatio = (float)window.getWidth() / window.getHeight();
            projectionMatrix.setPerspective(EngineOptions.getOptionAsFloat("FOV"), aspectRatio, EngineOptions.getOptionAsFloat("Z_NEAR"), EngineOptions.getOptionAsFloat("Z_FAR"));
            shader.setUniformData("projectionMatrix", projectionMatrix);
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        Map<String, IF_SceneObject> gameObjects = _scene.getGameObjects();
        Vector3 lightPosition = _scene.getLightPosition();

        //UPLOAD FRAME RELEVANT UNIFORMS HERE
        shader.setUniformData("lightPosition", lightPosition);

        //FILTER OBJECTS FOR FRUSTUM CULLING
        if(EngineOptions.getOptionAsBoolean("FRUSTUM_CULLING"))
        {
            viewProjectionMatrix.multiply(viewMatrix, projectionMatrix);

            //UPDATE FRUSTUM PLANES TODO: Add option to freeze update (don't update the planes)
            for(int i = 0; i < NUMBER_OF_FRUSTUM_PLANES; i++)
            {
                viewProjectionMatrix.calcFrustumPlane(i, frustumPlanes[i]);
            }

            for(IF_SceneObject sceneObject : gameObjects.values())
            {
                Transform temp = sceneObject.getTransform();

                //IS OBJECT INSIDE FRUSTUM?

                Vector3 position = temp.getPosition();
                boolean isInsideFrustum = true;
                for (int i = 0; i < NUMBER_OF_FRUSTUM_PLANES; i++)
                {
                    Vector4 plane = frustumPlanes[i];
                    if (plane.x * position.x + plane.y * position.y + plane.z * position.z + plane.w <= -sceneObject.getMesh().getBoundingRadius() )
                    {
                        isInsideFrustum = false;
                    }
                }

                temp.setVisibility(isInsideFrustum);
            }
        }

        //RENDER ALL VISIBLE OBJECTS
        Material material = _scene.getSceneMaterial();
        for(IF_SceneObject sceneObject : gameObjects.values())
        {
            Transform temp = sceneObject.getTransform();

            if(temp.isVisible())
            {
                OpenGLMesh mesh = sceneObject.getMesh();

                int indicesCount = mesh.getIndicesCount();
                totalVerticesInFrame += mesh.getVertexCount();

                glBindVertexArray(mesh.getVaoID());
                glEnableVertexAttribArray(OpenGLMesh.VERTICES);
                glEnableVertexAttribArray(OpenGLMesh.NORMALS);
                glEnableVertexAttribArray(OpenGLMesh.UV_COORDS);

                modelMatrix.setModelValues(temp.getPosition(), temp.getRotation(), temp.getScale());
                modelViewMatrix.multiply(modelMatrix, viewMatrix);
                shader.setUniformData("modelViewMatrix", modelViewMatrix);

                /* TODO: It's way more performant to do this only once in the init method.
                * But that would mean only on giant texture atlas and no texture changes during rendering.
                * Or a few atlasses and the material only holds an index for an atlas*/

                if(EngineOptions.getOptionAsBoolean("ENABLE_DIFFUSE_MAPPING") && material.hasDiffuseMap()) {
                    shader.setUniformData("hasDiffuseMap", 1);
                    glActiveTexture(GL_TEXTURE0);
                    glBindTexture(GL_TEXTURE_2D, material.getDiffuseMap().getID());
                }
                else {
                    shader.setUniformData("hasDiffuseMap", 0);
                }

                if(EngineOptions.getOptionAsBoolean("ENABLE_NORMAL_MAPPING") && material.hasNormalMap()) {
                    shader.setUniformData("hasNormalMap", 1);
                    glActiveTexture(GL_TEXTURE1);
                    glBindTexture(GL_TEXTURE_2D, material.getNormalMap().getID());
                }
                else {
                    shader.setUniformData("hasNormalMap", 0);
                }

                if(EngineOptions.getOptionAsBoolean("ENABLE_GLOSS_MAPPING") && material.hasGlossMap()) {
                    shader.setUniformData("hasGlossMap", 1);
                    glActiveTexture(GL_TEXTURE2);
                    glBindTexture(GL_TEXTURE_2D, material.getGlossMap().getID());
                }
                else {
                    shader.setUniformData("hasGlossMap", 0);
                }

                if(EngineOptions.getOptionAsBoolean("ENABLE_ILLUMINATION_MAPPING") && material.hasIlluminationMap()) {
                    shader.setUniformData("hasIlluminationMap", 1);
                    glActiveTexture(GL_TEXTURE3);
                    glBindTexture(GL_TEXTURE_2D, material.getIlluminationMap().getID());
                }
                else {
                    shader.setUniformData("hasIlluminationMap", 0);
                }
                /*****************************************************************************/

                if(EngineOptions.getOptionAsBoolean("SHOW_WIREFRAME"))
                {
                    glDrawElements(GL_TRIANGLES, indicesCount, GL_UNSIGNED_INT, 0);
                    glDrawElements(GL_POINTS, indicesCount, GL_UNSIGNED_INT, 0);
                }
                else
                {
                    glDrawElements(GL_TRIANGLES, indicesCount, GL_UNSIGNED_INT, 0);
                }

                glDisableVertexAttribArray(OpenGLMesh.VERTICES);
                glDisableVertexAttribArray(OpenGLMesh.NORMALS);
                glDisableVertexAttribArray(OpenGLMesh.UV_COORDS);
                glBindVertexArray(0);
            }
        }

        if(EngineOptions.getOptionAsBoolean("DEBUG_MODE"))
        {
            Logger.getInstance().logData("VERTEX COUNT", totalVerticesInFrame);
        }
    }

    private void setupOpenGl()
    {
        glClearColor(0.1f, 0.1f, 0.1f, 1.0f);

        glPointSize(EngineOptions.getOptionAsFloat("POINT_SIZE"));

        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LESS);

        glEnable(GL_STENCIL_TEST);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        if(EngineOptions.getOptionAsBoolean("BACK_FACE_CULLING"))
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

    public void cleanup()
    {
        Logger.getInstance().writeln(">> CLEANING UP RENDERER");

        if (shader != null)
        {
            shader.cleanup();
        }
    }
}