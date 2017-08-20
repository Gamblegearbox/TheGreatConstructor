package engine.core;

import engine.camera.Camera;
import engine.gameEntities.GameEntity;
import engine.interfaces.IHud;
import engine.light.DirectionalLight;
import engine.mesh.Mesh;
import engine.scene.Scene;
import engine.scene.SceneLight;
import engine.shading.ShaderProgram;
import engine.utils.Utils;
import game.Materials;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE2;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class Renderer {

    private static final float FOV = (float) Math.toRadians(60.0f);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.f;

    private static final float specularPower = 64f;
    private final Transformation transformation;

    private ShaderProgram sceneShaderProgram;
    private ShaderProgram solidColorShaderProgram;
    private ShaderProgram hudShaderProgram;

    public Renderer()
    {
        transformation = new Transformation();
    }

    public void init() throws Exception
    {
        setupOpenGL();
        setupShader();
        setupHudShader();
    }

    private void setupOpenGL()
    {
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_STENCIL_TEST);
        glEnable(GL_BLEND);

        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glPointSize(EngineOptions.POINT_SIZE);

        if(EngineOptions.WIREFRAME_MODE)
        {
            glClearColor(0.19f, 0.74f, 1.0f, 1.0f);
            glDepthFunc(GL_LEQUAL);
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
            glDisable(GL_CULL_FACE);
        }
        else
        {
            glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
            glDepthFunc(GL_LESS);
            glPolygonMode(GL_FRONT_FACE, GL_FILL);
            glEnable(GL_CULL_FACE);
            glCullFace(GL_BACK);
        }
    }

    private void setupShader() throws Exception
    {
        // Create shader
        sceneShaderProgram = new ShaderProgram();
        sceneShaderProgram.createVertexShader(Utils.loadResource("/shaders/scene.vs"));
        sceneShaderProgram.createFragmentShader(Utils.loadResource("/shaders/scene.fs"));
        sceneShaderProgram.link();

        solidColorShaderProgram = new ShaderProgram();
        solidColorShaderProgram.createVertexShader(Utils.loadResource("/shaders/solidColor.vs"));
        solidColorShaderProgram.createFragmentShader(Utils.loadResource("/shaders/solidColor.fs"));
        solidColorShaderProgram.link();

        // Create uniforms for modelView and projection matrices
        sceneShaderProgram.createUniform("projectionMatrix");
        sceneShaderProgram.createUniform("modelViewMatrix");
        sceneShaderProgram.createUniform("texture_sampler");

        solidColorShaderProgram.createUniform("projectionMatrix");
        solidColorShaderProgram.createUniform("modelViewMatrix");
        solidColorShaderProgram.createUniform("color");

        // Create uniform for material
        sceneShaderProgram.createMaterialUniform("material");

        // Create lighting related uniforms
        sceneShaderProgram.createUniform("specularPower");
        sceneShaderProgram.createUniform("ambientLight");
        sceneShaderProgram.createDirectionalLightUniform("directionalLight");
    }

    private void setupHudShader() throws Exception
    {
        hudShaderProgram = new ShaderProgram();
        hudShaderProgram.createVertexShader(Utils.loadResource("/shaders/hud.vs"));
        hudShaderProgram.createFragmentShader(Utils.loadResource("/shaders/hud.fs"));
        hudShaderProgram.link();

        // Create uniforms for Orthographic-model projection matrix and base colour
        hudShaderProgram.createUniform("projModelMatrix");
        hudShaderProgram.createUniform("colour");
        hudShaderProgram.createUniform("hasTexture");
    }

    public void render(Window window, Camera camera, Scene scene, IHud hud)
    {
        clear();

        glViewport(0, 0, window.getWidth(), window.getHeight());

        // Update projection and view matrices once per render cycle
        transformation.updateProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        transformation.updateViewMatrix(camera);

        renderScene(scene);
        if(!EngineOptions.WIREFRAME_MODE) {
            renderHud(window, hud);
        }
    }

    public void clear()
    {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
    }

    public void switchBackFaceCulling()
    {
        if(EngineOptions.CULL_BACK_FACE)
        {
            glEnable(GL_CULL_FACE);
        }
        else
        {
            glDisable(GL_CULL_FACE);
        }
    }

    public void renderScene(Scene scene)
    {
        if(EngineOptions.WIREFRAME_MODE)
        {
            solidColorShaderProgram.bind();

            Matrix4f projectionMatrix = transformation.getProjectionMatrix();
            solidColorShaderProgram.setUniform("projectionMatrix", projectionMatrix);

            Matrix4f viewMatrix = transformation.getViewMatrix();

            Map<Mesh, List<GameEntity>> mapMeshes = scene.getGameMeshes();

            // Render each mesh with the associated game Items
            for (Mesh mesh : mapMeshes.keySet()) {

                solidColorShaderProgram.setUniform("color", EngineOptions.LINE_COLOR);
                mesh.renderList(GL_TRIANGLES, mapMeshes.get(mesh), (GameEntity gameEntity) ->
                        {
                            Matrix4f modelMatrix = transformation.buildModelMatrix(gameEntity);
                            Matrix4f modelViewMatrix = transformation.buildModelViewMatrix(modelMatrix, viewMatrix);
                            solidColorShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
                        }
                );
                solidColorShaderProgram.setUniform("color", EngineOptions.POINT_COLOR);
                mesh.renderList(GL_POINTS, mapMeshes.get(mesh), (GameEntity gameEntity) ->
                        {
                            Matrix4f modelMatrix = transformation.buildModelMatrix(gameEntity);
                            Matrix4f modelViewMatrix = transformation.buildModelViewMatrix(modelMatrix, viewMatrix);
                            solidColorShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
                        }
                );
            }
            solidColorShaderProgram.unbind();
        }
        else
        {
            sceneShaderProgram.bind();

            Matrix4f projectionMatrix = transformation.getProjectionMatrix();
            sceneShaderProgram.setUniform("projectionMatrix", projectionMatrix);

            Matrix4f viewMatrix = transformation.getViewMatrix();

            SceneLight sceneLight = scene.getSceneLight();
            renderLights(viewMatrix, sceneLight);

            sceneShaderProgram.setUniform("texture_sampler", 0);
            Map<Mesh, List<GameEntity>> mapMeshes = scene.getGameMeshes();

            // Render each mesh with the associated game Items
            for (Mesh mesh : mapMeshes.keySet()) {
                if (EngineOptions.CAP_MATERIAL) {
                    sceneShaderProgram.setUniform("material", Materials.CAP_MAT);
                } else {
                    sceneShaderProgram.setUniform("material", mesh.getMaterial());
                }
                glActiveTexture(GL_TEXTURE2);
                mesh.renderList(GL_TRIANGLES, mapMeshes.get(mesh), (GameEntity gameEntity) ->
                        {
                            Matrix4f modelMatrix = transformation.buildModelMatrix(gameEntity);
                            Matrix4f modelViewMatrix = transformation.buildModelViewMatrix(modelMatrix, viewMatrix);
                            sceneShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
                        }
                );
            }
            sceneShaderProgram.unbind();
        }
    }

    private void renderLights(Matrix4f viewMatrix, SceneLight sceneLight)
    {
        sceneShaderProgram.setUniform("ambientLight", sceneLight.getAmbientLight());
        sceneShaderProgram.setUniform("specularPower", specularPower);

        // Get a copy of the directional light object and transform its position to view coordinates
        DirectionalLight currDirLight = new DirectionalLight(sceneLight.getDirectionalLight());
        Vector4f dir = new Vector4f(currDirLight.getDirection(), 0);
        dir.mul(viewMatrix);
        currDirLight.setDirection(new Vector3f(dir.x, dir.y, dir.z));
        sceneShaderProgram.setUniform("directionalLight", currDirLight);
    }

    private void renderHud(Window window, IHud hud)
    {
        if (hud != null) {
            hudShaderProgram.bind();

            Matrix4f ortho = transformation.getOrtho2DProjectionMatrix(0, window.getWidth(), window.getHeight(), 0);
            for (GameEntity gameEntity : hud.getGameEntities()) {
                Mesh mesh = gameEntity.getMesh();
                // Set ortohtaphic and model matrix for this HUD item
                Matrix4f projModelMatrix = transformation.buildOrthoProjModelMatrix(gameEntity, ortho);
                hudShaderProgram.setUniform("projModelMatrix", projModelMatrix);
                hudShaderProgram.setUniform("colour", gameEntity.getMesh().getMaterial().getColour());
                hudShaderProgram.setUniform("hasTexture", gameEntity.getMesh().getMaterial().isTextured() ? 1 : 0);

                // Render the mesh for this HUD item
                mesh.render();
            }

            hudShaderProgram.unbind();
        }
    }

    public void cleanup()
    {
        if (sceneShaderProgram != null)
        {
            sceneShaderProgram.cleanup();
        }

        if (hudShaderProgram != null)
        {
            hudShaderProgram.cleanup();
        }
    }
}
