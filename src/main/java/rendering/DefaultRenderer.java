package rendering;

import core.*;
import game.Assets;
import hud.Hud;
import game.Scene;
import hud.TextItem;
import interfaces.IF_SceneObject;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import utils.Logger;

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
    private final Transformation transformation;
    private Matrix4f projectionMatrix3D;
    private Matrix4f projectionMatrixHUD;

    private final List<ShaderProgram> matCapShaders;

    private ShaderProgram hudShader;
    private ShaderProgram activeMatCapShader;
    private Material materialAtlas;
    private Texture lightColorGradient;
    private int activeMatCapShaderIndex = 0;


    public DefaultRenderer(Window _window)
    {
        this.window = _window;
        matCapShaders = new ArrayList<>();
        transformation = new Transformation();

        projectionMatrix3D = new Matrix4f();
        projectionMatrixHUD = new Matrix4f();
/*
        frustumPlanes = new Vector4f[6];
        for (int i = 0; i < 6; i++)
        {
            frustumPlanes[i] = new Vector4f();
        }
        */
    }

    public void init()
    {
        Logger.getInstance().writeln(">> INITIALISING RENDERER");
        logGpuInfo();

        materialAtlas = new Material(Assets.ATLAS_COLORS, null, Assets.ATLAS_GLOSS, Assets.ATLAS_EMIT);
        lightColorGradient = Assets.GRADIENT_LIGHT_COLORS;

        projectionMatrix3D = transformation.getPerspectiveProjectionMatrix(EngineOptions.INITIAL_FOV, window.getWidth(), window.getHeight(), EngineOptions.Z_NEAR, EngineOptions.Z_FAR);
        projectionMatrixHUD = transformation.getOrthographicProjectionMatrix(0, window.getWidth(), window.getHeight(), 0);

        setupOpenGl();
        loadShaders();

        activeMatCapShader = matCapShaders.get(activeMatCapShaderIndex);
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

        if(EngineOptions.BACK_FACE_CULLING)
        {
            glEnable(GL_CULL_FACE);
            glCullFace(GL_BACK);
        }
        else
        {
            glDisable(GL_CULL_FACE);
        }

        glPolygonMode(GL_FRONT_FACE, GL_FILL);


    }

    private void loadShaders()
    {
        //SCENE SHADERS
        Assets.SHADER_SCENE.bind();
        Assets.SHADER_SCENE.setUniformData("diffuseMap_sampler", Texture.DIFFUSE);
        Assets.SHADER_SCENE.setUniformData("normalMap_sampler", Texture.NORMALS);
        Assets.SHADER_SCENE.setUniformData("glossMap_sampler", Texture.GLOSS);
        Assets.SHADER_SCENE.setUniformData("illuminationMap_sampler", Texture.ILLUMINATION);
        Assets.SHADER_SCENE.setUniformData("lightColor_sampler", Texture.GRADIENT_LIGHT_COLOR);

        //TODO implement water shader (thoughts: shaders bound to meshes? Sort meshes by shader? how to render debug overlay)
        Assets.SHADER_SCENE_WATER.bind();
        Assets.SHADER_SCENE.setUniformData("diffuseMap_sampler", Texture.DIFFUSE);
        Assets.SHADER_SCENE.setUniformData("normalMap_sampler", Texture.NORMALS);
        Assets.SHADER_SCENE.setUniformData("glossMap_sampler", Texture.GLOSS);
        Assets.SHADER_SCENE.setUniformData("illuminationMap_sampler", Texture.ILLUMINATION);
        Assets.SHADER_SCENE.setUniformData("lightColor_sampler", Texture.GRADIENT_LIGHT_COLOR);

        //MAT CAP SHADERS
        matCapShaders.add(null);
        Assets.SHADER_DEBUG_NORMALS_TO_COLOR.bind();
        Assets.SHADER_DEBUG_NORMALS_TO_COLOR.setUniformData("normalMap_sampler", Texture.NORMALS);
        matCapShaders.add(Assets.SHADER_DEBUG_NORMALS_TO_COLOR);

        matCapShaders.add(Assets.SHADER_DEBUG_SOLID_WIREFRAME);

        matCapShaders.add(Assets.SHADER_DEBUG_DEPTH_TO_COLOR);

        Assets.HUD_SHADER.bind();
        Assets.HUD_SHADER.setUniformData("diffuseMap_sampler", Texture.DIFFUSE);
        hudShader = Assets.HUD_SHADER;
    }


    public void switchShader(){
        activeMatCapShaderIndex++;
        activeMatCapShaderIndex %= matCapShaders.size();
        activeMatCapShader = matCapShaders.get(activeMatCapShaderIndex);
    }

    public void render(Scene _scene, Vector3f _lightPosition, float _dayTime, Camera _camera, Hud _hud) {

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);

        if ( window.isResized() )
        {
            projectionMatrix3D = transformation.getPerspectiveProjectionMatrix(_camera.getFieldOfView(), window.getWidth(), window.getHeight(), EngineOptions.Z_NEAR, EngineOptions.Z_FAR);
            projectionMatrixHUD = transformation.getOrthographicProjectionMatrix(0, window.getWidth(), window.getHeight(), 0);
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        renderScene(_scene, _lightPosition, _dayTime, _camera);
        renderHud(_hud);
    }

    private void renderScene(Scene _scene, Vector3f _lightPosition, float _dayTime, Camera _camera){

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
        glActiveTexture(GL_TEXTURE4);
        glBindTexture(GL_TEXTURE_2D, lightColorGradient.getID());


        int verticesInScene = 0;

        //TODO: OBJECT OR MAT CAP SHADER
        ShaderProgram currentShader;
        if(activeMatCapShader != null){
            currentShader = activeMatCapShader;
        }
        else{
            currentShader = Assets.SHADER_SCENE;
        }
        currentShader.bind();
        //TODO:END OF TODO//////////////

        Matrix4f viewMatrix = transformation.getViewMatrix(_camera);

        //UPLOAD FRAME RELEVANT UNIFORMS HERE
        currentShader.setUniformData("projectionMatrix", projectionMatrix3D);
        currentShader.setUniformData("viewMatrix", viewMatrix);
        currentShader.setUniformData("lightPosition", _lightPosition);
        currentShader.setUniformData("timeOfDay", _dayTime);
        currentShader.setUniformData("cameraPosition", _camera.getPosition());

        Map<String, IF_SceneObject> gameObjects = _scene.getGameObjects();
        /*
        //FILTER OBJECTS FOR FRUSTUM CULLING
        if(EngineOptions.FRUSTUM_CULLING)
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
            Transform transform = sceneObject.getTransform();
            Mesh mesh = sceneObject.getMesh();

            if(mesh.isVisible())
            {
                verticesInScene += mesh.getVertexCount();

                Matrix4f modelMatrix = transformation.getModelMatrix(transform);
                currentShader.setUniformData("modelMatrix", modelMatrix);

                glBindVertexArray(mesh.getVaoID());
                glEnableVertexAttribArray(Mesh.VERTICES);
                glEnableVertexAttribArray(Mesh.NORMALS);
                glEnableVertexAttribArray(Mesh.UV_COORDS);

                //TODO: add timed steps function
                glDrawElements(GL_TRIANGLES, mesh.getIndicesCount(), GL_UNSIGNED_INT, 0);

                glDisableVertexAttribArray(Mesh.VERTICES);
                glDisableVertexAttribArray(Mesh.NORMALS);
                glDisableVertexAttribArray(Mesh.UV_COORDS);
                glBindVertexArray(0);
            }
        }

        currentShader.unbind();

        if(EngineOptions.DEBUG_MODE)
        {
            Logger.getInstance().logData("VERTEX COUNT SCENE", verticesInScene);
        }

    }

    public void renderHud(Hud _hud){

        int verticesInHud = 0;

        hudShader.bind();
        hudShader.setUniformData("fontColor", 1.0f, 1.0f, 1.0f, 1.0f);
        hudShader.setUniformData("labelColor", 1.0f, 0.0f, 0.0f, 0.5f);
        hudShader.setUniformData("projectionMatrix", projectionMatrixHUD);

        Map<String, TextItem> hudObjects = _hud.getHudItems();

        for(TextItem hudItem : hudObjects.values()) {

            Mesh mesh = hudItem.getMesh();
            verticesInHud += mesh.getVertexCount();

            Transform transform = hudItem.getTransform();

            Matrix4f modelMatrix = transformation.getModelMatrix(transform);
            hudShader.setUniformData("modelMatrix", modelMatrix);

            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, hudItem.getFontTexture().getTexture().getID());

            glBindVertexArray(mesh.getVaoID());
            glEnableVertexAttribArray(Mesh.VERTICES);
            glEnableVertexAttribArray(Mesh.NORMALS);
            glEnableVertexAttribArray(Mesh.UV_COORDS);

            glDrawElements(GL_TRIANGLES, mesh.getIndicesCount(), GL_UNSIGNED_INT, 0);

            glDisableVertexAttribArray(Mesh.VERTICES);
            glDisableVertexAttribArray(Mesh.NORMALS);
            glDisableVertexAttribArray(Mesh.UV_COORDS);
            glBindVertexArray(0);
        }

        hudShader.unbind();

        if(EngineOptions.DEBUG_MODE)
        {
            Logger.getInstance().logData("VERTEX COUNT HUD", verticesInHud);
        }
    }

    public int getActiveShaderIndex(){
        return activeMatCapShaderIndex;
    }

    public void cleanup() {
        Logger.getInstance().writeln(">> CLEANING UP RENDERER");

        hudShader.cleanup();

        for(ShaderProgram shader : matCapShaders){
            if(shader != null) {
                shader.cleanup();
            }
        }

    }
}
