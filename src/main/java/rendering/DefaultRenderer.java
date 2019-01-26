package rendering;

import core.*;
import game.Assets;
import interfaces.IF_SceneObject;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
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

public class DefaultRenderer {

    private static final int NUMBER_OF_FRUSTUM_PLANES = 6;

    private final Vector4f CLEAR_COLOR = new Vector4f(0.1f, 0.1f, 0.1f, 1.0f);

    private final Window window;
    private Transformation transformation;
    private Matrix4f projectionMatrix = new Matrix4f();

    //SHADER AND MATERIAL STUFF TODO: put somewhere more global or stick with renderer?
    private final List<ShaderProgram> availableShaders;
    private ShaderProgram activeShader;
    private Material materialAtlas;
    private Texture shadingGradient;
    private Texture lightColorGradient;
    private int currentShaderIndex = 0;


    public DefaultRenderer(Window _window)
    {
        this.window = _window;
        availableShaders = new ArrayList<>();
        transformation = new Transformation();

/*
        frustumPlanes = new Vector4f[6];
        for (int i = 0; i < 6; i++)
        {
            frustumPlanes[i] = new Vector4f();
        }
        */
    }

    public void init() throws Exception
    {
        Logger.getInstance().writeln(">> INITIALISING RENDERER");
        logGpuInfo();

        projectionMatrix = transformation.getProjectionMatrix(EngineOptions.getOptionAsFloat("FOV"), window.getWidth(), window.getHeight(), EngineOptions.getOptionAsFloat("Z_NEAR"), EngineOptions.getOptionAsFloat("Z_FAR"));

        materialAtlas = new Material(Assets.ATLAS_COLORS, null, Assets.ATLAS_GLOSS, Assets.ATLAS_EMIT);
        shadingGradient = Assets.GRADIENT_SHADING;
        lightColorGradient = Assets.GRADIENT_LIGHT_COLORS;

        setupOpenGl();
        loadShaders();
        activateShader(currentShaderIndex);
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
        glClearColor(CLEAR_COLOR.x, CLEAR_COLOR.y, CLEAR_COLOR.z, CLEAR_COLOR.w);

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
                "./res/shaders/VERT.glsl;./res/shaders/GEOM.glsl;./res/shaders/FRAG.glsl",
                "./res/shaders/VERT.glsl;./res/shaders/GEOM.glsl;./res/shaders/debug/FRAG_normalToColor.glsl",
                "./res/shaders/VERT.glsl;./res/shaders/GEOM.glsl;./res/shaders/debug/FRAG_depthToColor.glsl"
        };

        for(String path : shadersToLoad) {
            String[] pathTokens = path.split(";");
            String vertexShaderPath = pathTokens[0];
            String geometryShaderPath = pathTokens[1];
            String fragmentShaderPath = pathTokens[2];

            ShaderProgram shader = new ShaderProgram();
            shader.createVertexShader(Utils.loadResource(vertexShaderPath));
            shader.createGeometryShader(Utils.loadResource(geometryShaderPath));
            shader.createFragmentShader(Utils.loadResource(fragmentShaderPath));
            shader.link();

            availableShaders.add(shader);
        }
    }

    private void activateShader(int _index){
        activeShader = availableShaders.get(_index);

        activeShader.bind();

        activeShader.setUniformData("diffuseMap_sampler", Texture.DIFFUSE);
        activeShader.setUniformData("normalMap_sampler", Texture.NORMALS);
        activeShader.setUniformData("glossMap_sampler", Texture.GLOSS);
        activeShader.setUniformData("illuminationMap_sampler", Texture.ILLUMINATION);
        activeShader.setUniformData("shading_sampler", Texture.GRADIENT_SHADING);
        activeShader.setUniformData("lightColor_sampler", Texture.GRADIENT_LIGHT_COLOR);

        //Upload textures
        if(materialAtlas.getDiffuseMap() != null) {
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, materialAtlas.getDiffuseMap().getID());
        }

        if(materialAtlas.getNormalMap() != null) {
            glActiveTexture(GL_TEXTURE1);
            glBindTexture(GL_TEXTURE_2D, materialAtlas.getNormalMap().getID());
        }

        if(materialAtlas.getGlossMap() != null) {
            glActiveTexture(GL_TEXTURE2);
            glBindTexture(GL_TEXTURE_2D, materialAtlas.getGlossMap().getID());
        }

        if(materialAtlas.getIlluminationMap() != null) {
            glActiveTexture(GL_TEXTURE3);
            glBindTexture(GL_TEXTURE_2D, materialAtlas.getIlluminationMap().getID());
        }

    }

    public void switchShader(){
        currentShaderIndex++;
        currentShaderIndex%=availableShaders.size();
        activateShader(currentShaderIndex);
    }

    public void render(Scene _scene, Vector3f _lightPosition, float _dayTime, Camera _camera) {

        int totalVerticesInFrame = 0;
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);

        projectionMatrix = transformation.getProjectionMatrix(EngineOptions.getOptionAsFloat("FOV"), window.getWidth(), window.getHeight(), EngineOptions.getOptionAsFloat("Z_NEAR"), EngineOptions.getOptionAsFloat("Z_FAR"));
        activeShader.setUniformData("projectionMatrix", projectionMatrix);

        if ( window.isResized() )
        {
            projectionMatrix = transformation.getProjectionMatrix(EngineOptions.getOptionAsFloat("FOV"), window.getWidth(), window.getHeight(), EngineOptions.getOptionAsFloat("Z_NEAR"), EngineOptions.getOptionAsFloat("Z_FAR"));
            activeShader.setUniformData("projectionMatrix", projectionMatrix);
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        Matrix4f viewMatrix = transformation.getViewMatrix(_camera);

        Map<String, IF_SceneObject> gameObjects = _scene.getGameObjects();

        //UPLOAD FRAME RELEVANT UNIFORMS HERE
        activeShader.setUniformData("lightPosition", _lightPosition);
        activeShader.setUniformData("timeOfDay", _dayTime);
        activeShader.setUniformData("viewMatrix", viewMatrix);

        //LOADING GRADIENTS
        glActiveTexture(GL_TEXTURE4);
        glBindTexture(GL_TEXTURE_2D, shadingGradient.getID());
        glActiveTexture(GL_TEXTURE5);
        glBindTexture(GL_TEXTURE_2D, lightColorGradient.getID());

        /*
        //FILTER OBJECTS FOR FRUSTUM CULLING
        if(EngineOptions.getOptionAsBoolean("FRUSTUM_CULLING"))
        {
            //viewMatrix.mul(projectionMatrix, viewProjectionMatrix);

            //UPDATE FRUSTUM PLANES TODO: Add option to freeze update (don't update the planes)
            for(int i = 0; i < NUMBER_OF_FRUSTUM_PLANES; i++)
            {
                viewProjectionMatrix.frustumPlane(i, frustumPlanes[i]);
            }

            for(IF_SceneObject sceneObject : gameObjects.values())
            {
                Transform temp = sceneObject.getTransform();

                //IS OBJECT INSIDE FRUSTUM?
                Vector3f position = temp.getPosition();
                boolean isInsideFrustum = true;
                for (int i = 0; i < NUMBER_OF_FRUSTUM_PLANES; i++)
                {
                    Vector4f plane = frustumPlanes[i];
                    if (plane.x * position.x + plane.y * position.y + plane.z * position.z + plane.w <= -sceneObject.getMesh().getBoundingRadius() )
                    {
                        isInsideFrustum = false;
                    }
                }

                temp.setVisibility(isInsideFrustum);
            }
        }*/

        //RENDER ALL VISIBLE OBJECTS
        for(IF_SceneObject sceneObject : gameObjects.values())
        {
            Transform temp = sceneObject.getTransform();

            if(temp.isVisible())
            {
                OpenGLMesh mesh = sceneObject.getMesh();
                totalVerticesInFrame += mesh.getVertexCount();

                Matrix4f modelMatrix = transformation.getModelMatrix(temp);
                activeShader.setUniformData("modelMatrix", modelMatrix);

                glBindVertexArray(mesh.getVaoID());
                glEnableVertexAttribArray(OpenGLMesh.VERTICES);
                glEnableVertexAttribArray(OpenGLMesh.NORMALS);
                glEnableVertexAttribArray(OpenGLMesh.UV_COORDS);

                glDrawElements(GL_TRIANGLES, mesh.getIndicesCount(), GL_UNSIGNED_INT, 0);

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

    public void cleanup() {
        Logger.getInstance().writeln(">> CLEANING UP RENDERER");

        for(ShaderProgram shader : availableShaders){
            shader.cleanup();
        }
    }
}
