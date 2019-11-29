package rendering;

import core.*;
import game.Assets;
import hud.Hud;
import game.Scene;
import hud.TextItem;
import interfaces.IF_HudItem;
import interfaces.IF_SceneItem;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import utils.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.GL_SHADING_LANGUAGE_VERSION;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class Renderer {

    private static final int NUMBER_OF_FRUSTUM_PLANES = 6;
    private final Vector4f CLEAR_COLOR = new Vector4f(0.15f, 0.15f, 0.15f, 1.0f);

    private final Window window;
    private final Transformation transformation;
    private final List<ShaderProgram> matCapShaders;
    private final Vector4f[] frustumPlanes;

    private Matrix4f projectionMatrix3D;
    private Matrix4f projectionMatrixHUD;

    //private ShaderProgram hudShader;
    private ShaderProgram activeMatCapShader;
    private Material sceneMaterial;

    private int activeMatCapShaderIndex = 0;
    private float anima = 0;

    public Renderer(Window _window)
    {
        this.window = _window;
        matCapShaders = new ArrayList<>();
        transformation = new Transformation();

        projectionMatrix3D = new Matrix4f();
        projectionMatrixHUD = new Matrix4f();

        frustumPlanes = new Vector4f[NUMBER_OF_FRUSTUM_PLANES];
        for (int i = 0; i < NUMBER_OF_FRUSTUM_PLANES; i++)
        {
            frustumPlanes[i] = new Vector4f();
        }
    }

    public void init()
    {
        Logger.getInstance().writeln(">> INITIALISING RENDERER");
        logGpuInfo();

        projectionMatrix3D = transformation.getPerspectiveProjectionMatrix(EngineOptions.INITIAL_FOV, window.getWidth(), window.getHeight(), EngineOptions.Z_NEAR, EngineOptions.Z_FAR);
        projectionMatrixHUD = transformation.getOrthographicProjectionMatrix(0, window.getWidth(), window.getHeight(), 0);

        sceneMaterial = new Material(Assets.ATLAS_COLORS, Assets.ATLAS_NORMALS, Assets.ATLAS_MATERIAL_INFO, Assets.SHADOW_PATTERN, Assets.REFLECTION_MAP_DAY, Assets.GRADIENT_LIGHT_COLORS);

        initOpenGl();
        initShaders();

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

    private void initOpenGl()
    {
        glClearColor(CLEAR_COLOR.x, CLEAR_COLOR.y, CLEAR_COLOR.z, CLEAR_COLOR.w);

        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LESS);

        glEnable(GL_STENCIL_TEST);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        if(EngineOptions.BACK_FACE_CULLING) {
            glEnable(GL_CULL_FACE);
            glCullFace(GL_BACK);
        }
        else {
            glDisable(GL_CULL_FACE);
        }

        glPolygonMode(GL_FRONT_FACE, GL_FILL);
    }

    private void initShaders()
    {
        //SCENE SHADERS
        Assets.SHADER_DEBUG_TEST.bind();
        Assets.SHADER_DEBUG_TEST.setUniformData("diffuseMap_sampler", Texture.RGBA_0);
        Assets.SHADER_DEBUG_TEST.setUniformData("normalMap_sampler", Texture.RGBA_1);
        Assets.SHADER_DEBUG_TEST.setUniformData("materialInfo_sampler", Texture.RGBA_2);
        Assets.SHADER_DEBUG_TEST.setUniformData("shadowPattern_sampler", Texture.RGBA_3);
        Assets.SHADER_DEBUG_TEST.setUniformData("reflectionMap_sampler", Texture.RGBA_4);
        Assets.SHADER_DEBUG_TEST.setUniformData("lightColorMap_sampler", Texture.RGBA_5);
        Assets.SHADER_DEBUG_TEST.setUniformData("windowSize", EngineOptions.WINDOW_WIDTH, EngineOptions.WINDOW_HEIGHT);

        Assets.SHADER_HUD.bind();
        Assets.SHADER_HUD.setUniformData("diffuseMap_sampler", Texture.RGBA_0);

        //MAT CAP SHADERS
        Assets.SHADER_DEBUG_NORMALS_TO_COLOR.bind();
        Assets.SHADER_DEBUG_NORMALS_TO_COLOR.setUniformData("normalMap_sampler", Texture.RGBA_1);

        matCapShaders.add(null); //first entry is null to mark the point when to use object shaders
        matCapShaders.add(Assets.SHADER_DEBUG_NORMALS_TO_COLOR);
        matCapShaders.add(Assets.SHADER_DEBUG_SOLID_WIREFRAME);
        matCapShaders.add(Assets.SHADER_DEBUG_DEPTH_TO_COLOR);
    }

    public void switchShader(){
        activeMatCapShaderIndex++;
        activeMatCapShaderIndex %= matCapShaders.size();
        activeMatCapShader = matCapShaders.get(activeMatCapShaderIndex);
    }

    public void render(Scene _scene, Hud _hud, Camera _camera, Vector3f _lightPosition, float _dayTime, float _deltaTime) {
        anima += _deltaTime;
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);

        if ( window.isResized() ) {
            projectionMatrix3D = transformation.getPerspectiveProjectionMatrix(_camera.getFieldOfView(), window.getWidth(), window.getHeight(), EngineOptions.Z_NEAR, EngineOptions.Z_FAR);
            projectionMatrixHUD = transformation.getOrthographicProjectionMatrix(0, window.getWidth(), window.getHeight(), 0);
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        renderScene(_scene, _camera, _lightPosition, _dayTime);
        renderHud(_hud);
    }

    private void renderScene(Scene _scene, Camera _camera, Vector3f _lightPosition, float _dayTime){
        frustumCullingCheck(_scene.getSceneItems().values());

        ShaderProgram shader;
        int verticesInRenderPass = 0;
        for(IF_SceneItem sceneObject : _scene.getSceneItems().values()) {

            shader = (activeMatCapShader != null) ? activeMatCapShader : sceneObject.getShader();
            shader.bind();

            shader.setUniformData("projectionMatrix", projectionMatrix3D);
            shader.setUniformData("viewMatrix", transformation.getViewMatrix(_camera));
            shader.setUniformData("lightPosition", _lightPosition);
            shader.setUniformData("timeOfDay", _dayTime);
            shader.setUniformData("cameraPosition", _camera.getPosition());
            shader.setUniformData("anima", anima);
            shader.setUniformData("illumination", sceneObject.getIllumination());

            activateMaterialTextures(sceneMaterial);
            verticesInRenderPass += renderSceneItem(sceneObject, shader);

            shader.unbind();
        }

        if(EngineOptions.DEBUG_MODE) {
            Logger.getInstance().logData("VERTEX COUNT SCENE", verticesInRenderPass);
        }
    }

    public void renderHud(Hud _hud){
        ShaderProgram shader;
        int verticesInRenderPass = 0;
        for(TextItem item : _hud.getHudItems().values()) {

            shader = item.getShader();
            shader.bind();

            shader.setUniformData("fontColor", 1.0f, 1.0f, 1.0f, 1.0f);
            shader.setUniformData("labelColor", 1.0f, 0.0f, 0.0f, 0.5f);
            shader.setUniformData("projectionMatrix", projectionMatrixHUD);

            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, item.getFontTexture().getTexture().getID());

            verticesInRenderPass += renderHudItem(item, shader);

            shader.unbind();
        }

        if(EngineOptions.DEBUG_MODE) {
            Logger.getInstance().logData("VERTEX COUNT HUD", verticesInRenderPass);
        }
    }

    private void activateMaterialTextures(Material _material){
        if(_material.getMap_0() != null) {
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, _material.getMap_0().getID());
        }

        if(_material.getMap_1() != null) {
            glActiveTexture(GL_TEXTURE1);
            glBindTexture(GL_TEXTURE_2D, _material.getMap_1().getID());
        }

        if(_material.getMap_2() != null) {
            glActiveTexture(GL_TEXTURE2);
            glBindTexture(GL_TEXTURE_2D, _material.getMap_2().getID());
        }

        if(_material.getMap_3() != null) {
            glActiveTexture(GL_TEXTURE3);
            glBindTexture(GL_TEXTURE_2D, _material.getMap_3().getID());
        }

        if(_material.getMap_4() != null) {
            glActiveTexture(GL_TEXTURE4);
            glBindTexture(GL_TEXTURE_2D, _material.getMap_4().getID());
        }

        if(_material.getMap_5() != null) {
            glActiveTexture(GL_TEXTURE5);
            glBindTexture(GL_TEXTURE_2D, _material.getMap_5().getID());
        }
    }

    private void frustumCullingCheck(Collection<IF_SceneItem> gameObjects){
        if(EngineOptions.FRUSTUM_CULLING)
        {
            Matrix4f viewProjectionMatrix = transformation.getViewProjectionMatrix();

            //UPDATE FRUSTUM PLANES TODO: Add option to freeze update (don't update the planes)
            for(int i = 0; i < NUMBER_OF_FRUSTUM_PLANES; i++) {
                viewProjectionMatrix.frustumPlane(i, frustumPlanes[i]);
            }

            for(IF_SceneItem sceneObject : gameObjects) {
                //IS OBJECT INSIDE FRUSTUM?
                Vector3f position = sceneObject.getTransform().getPosition();
                boolean isInsideFrustum = true;
                for (int i = 0; i < NUMBER_OF_FRUSTUM_PLANES; i++) {
                    Vector4f plane = frustumPlanes[i];
                    if (plane.x * position.x + plane.y * position.y + plane.z * position.z + plane.w <= -sceneObject.getMesh().getBoundingRadius() ) {
                        isInsideFrustum = false;
                    }
                }

                sceneObject.getMesh().setVisibility(isInsideFrustum);
            }
        }
    }

    private int renderSceneItem(IF_SceneItem _item, ShaderProgram _shader){

        int vertices = 0;
        Transform transform = _item.getTransform();
        Mesh mesh = _item.getMesh();

        if(mesh.isVisible()) {
            vertices = mesh.getVertexCount();

            Matrix4f modelMatrix = transformation.getModelMatrix(transform);
            _shader.setUniformData("modelMatrix", modelMatrix);

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
        return vertices;
    }

    private int renderHudItem(IF_HudItem _item, ShaderProgram _shader){

        int vertices = 0;
        Transform transform = _item.getTransform();
        Mesh mesh = _item.getMesh();

        if(mesh.isVisible()) {
            vertices = mesh.getVertexCount();

            Matrix4f modelMatrix = transformation.getModelMatrix(transform);
            _shader.setUniformData("modelMatrix", modelMatrix);

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
        return vertices;
    }

    public void cleanup() {
        Logger.getInstance().writeln(">> CLEANING UP RENDERER");

        for(ShaderProgram shader : matCapShaders){
            if(shader != null) {
                shader.cleanup();
            }
        }
    }
}
