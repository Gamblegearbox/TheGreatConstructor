package rendering;

import core.*;
import interfaces.IF_SceneObject;
import math.Matrix4;
import math.Vector3;
import math.Vector4;
import utils.Logger;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.GL_SHADING_LANGUAGE_VERSION;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class OpenGLRenderer {

    public static final int DEFAULT_SHADER = 0;
    public static final int NORMALS_SHADER = 1;
    public static final int DEPTH_SHADER = 2;

    private static final int NUMBER_OF_FRUSTUM_PLANES = 6;

    private final Window window;

    private final Matrix4 projectionMatrix;
    private final Matrix4 modelMatrix;
    private final Matrix4 viewMatrix;
    private final Matrix4 modelViewMatrix;
    private final Matrix4 viewProjectionMatrix;

    private final Vector4[] frustumPlanes;
    private final List<ShaderProgram> availableShaders;
    private ShaderProgram activeShader;

    public OpenGLRenderer(Window _window)
    {
        this.window = _window;
        availableShaders = new ArrayList<>();
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
        loadShaders();
        activateShader(DEFAULT_SHADER);
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

        if(EngineOptions.getOptionAsBoolean("SHOW_WIREFRAME"))
        {
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        }
        else
        {
            glPolygonMode(GL_FRONT_FACE, GL_FILL);
        }

    }

    private void loadShaders() throws Exception
    {
        String[] shadersToLoad = new String[]{
                "/shaders/shaded.vs;/shaders/shaded.fs",
                "/shaders/shaded.vs;/shaders/debug/normals.fs",
                "/shaders/shaded.vs;/shaders/debug/depth.fs"
        };

        for(String path : shadersToLoad) {
            String[] pathTokens = path.split(";");
            String vertexShaderPath = pathTokens[0];
            String fragmentShaderPath = pathTokens[1];

            ShaderProgram shader = new ShaderProgram();
            shader.createVertexShader(Utils.loadResource(vertexShaderPath));
            shader.createFragmentShader(Utils.loadResource(fragmentShaderPath));
            shader.link();

            availableShaders.add(shader);
        }
    }

    public void activateShader(int _index){
        activeShader =  availableShaders.get(_index);

        activeShader.bind();
        activeShader.setUniformData("projectionMatrix", projectionMatrix);

        activeShader.setUniformData("diffuseMap_sampler", Texture.DIFFUSE);
        activeShader.setUniformData("normalMap_sampler", Texture.NORMALS);
        activeShader.setUniformData("glossMap_sampler", Texture.GLOSS);
        activeShader.setUniformData("illuminationMap_sampler", Texture.ILLUMINATION);
    }

    public void render(Scene _scene)
    {
        int totalVerticesInFrame = 0;
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);

        if ( window.isResized() )
        {
            float aspectRatio = (float)window.getWidth() / window.getHeight();
            projectionMatrix.setPerspective(EngineOptions.getOptionAsFloat("FOV"), aspectRatio, EngineOptions.getOptionAsFloat("Z_NEAR"), EngineOptions.getOptionAsFloat("Z_FAR"));
            activeShader.setUniformData("projectionMatrix", projectionMatrix);
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        Map<String, IF_SceneObject> gameObjects = _scene.getGameObjects();
        Vector3 lightPosition = _scene.getLightPosition();

        //UPLOAD FRAME RELEVANT UNIFORMS HERE
        activeShader.setUniformData("lightPosition", lightPosition);

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
                activeShader.setUniformData("modelViewMatrix", modelViewMatrix);

                /* TODO: It's way more performant to do this only once in the init method.
                * But that would mean only on giant texture atlas and no texture changes during rendering.
                * Or a few atlasses and the material only holds an index for an atlas*/

                if(EngineOptions.getOptionAsBoolean("ENABLE_DIFFUSE_MAPPING") && material.hasDiffuseMap()) {
                    activeShader.setUniformData("hasDiffuseMap", 1);
                    glActiveTexture(GL_TEXTURE0);
                    glBindTexture(GL_TEXTURE_2D, material.getDiffuseMap().getID());
                }
                else {
                    activeShader.setUniformData("hasDiffuseMap", 0);
                }

                if(EngineOptions.getOptionAsBoolean("ENABLE_NORMAL_MAPPING") && material.hasNormalMap()) {
                    activeShader.setUniformData("hasNormalMap", 1);
                    glActiveTexture(GL_TEXTURE1);
                    glBindTexture(GL_TEXTURE_2D, material.getNormalMap().getID());
                }
                else {
                    activeShader.setUniformData("hasNormalMap", 0);
                }

                if(EngineOptions.getOptionAsBoolean("ENABLE_GLOSS_MAPPING") && material.hasGlossMap()) {
                    activeShader.setUniformData("hasGlossMap", 1);
                    glActiveTexture(GL_TEXTURE2);
                    glBindTexture(GL_TEXTURE_2D, material.getGlossMap().getID());
                }
                else {
                    activeShader.setUniformData("hasGlossMap", 0);
                }

                if(EngineOptions.getOptionAsBoolean("ENABLE_ILLUMINATION_MAPPING") && material.hasIlluminationMap()) {
                    activeShader.setUniformData("hasIlluminationMap", 1);
                    glActiveTexture(GL_TEXTURE3);
                    glBindTexture(GL_TEXTURE_2D, material.getIlluminationMap().getID());
                }
                else {
                    activeShader.setUniformData("hasIlluminationMap", 0);
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



    public void cleanup()
    {
        Logger.getInstance().writeln(">> CLEANING UP RENDERER");

        for(ShaderProgram shader : availableShaders){
            shader.cleanup();
        }
    }
}
