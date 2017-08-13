package game;

import engine.camera.Camera;
import engine.core.EngineOptions;
import engine.core.Renderer;
import engine.core.Window;
import engine.gameEntities.GameEntity;
import engine.input.KeyboardInput;
import engine.input.MouseInput;
import engine.interfaces.IGameLogic;
import engine.light.DirectionalLight;
import engine.mesh.Mesh;
import engine.scene.Scene;
import engine.scene.SceneLight;
import engine.shading.Material;
import engine.utils.OBJLoader;
import org.joml.Math;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;

public class Game implements IGameLogic {

    private static final float MOUSE_SENSITIVITY = 0.2f;

    private static final float CAMERA_SPEED = 5f;
    private static final float CAMERA_SPEED_FAST = 10f;

    private final Renderer renderer;
    private final Camera camera;
    private final Vector3f cameraIncrement;

    private Vector3f lightDirection;
    private float directionalLightAngle;
    private boolean isNight = false;

    private Scene scene;
    private Hud hud;

    private GameEntity allSpark_01;
    private GameEntity allSpark_02;
    private GameEntity allSpark_03;
    private float xRot = 0, yRot = 0, zRot = 0;

    //DEBUG VALUES
    private int totalUpdates = 0;
    private int totalRenderCycles = 0;
    private int totalInputCalls = 0;

    public Game()
    {
        renderer = new Renderer();
        camera = new Camera();
        cameraIncrement = new Vector3f(0, 0, 0);
        directionalLightAngle = 35;
    }

    @Override
    public void init(Window window) throws Exception
    {
        renderer.init();
        scene = new Scene();

        setupGameObjects();
        setupLight();
        setupCamera();
        setupHUD();
    }

    private void setupGameObjects() throws Exception
    {
        ArrayList<GameEntity> gameEntities = new ArrayList<>();

        // load color palette and apply it to material
        //Texture texture = new Texture("/textures/colorsFromPicture.png");

        Mesh mesh = OBJLoader.loadMesh("/models/REF_ONE_CUBIC_METER.obj");
        mesh.setMaterial(new Material(new Vector3f(0.7f, 0.02f, 0.03f), 0.5f));
        allSpark_01 = new GameEntity(mesh);

        mesh = OBJLoader.loadMesh("/models/REF_ONE_CUBIC_METER.obj");
        mesh.setMaterial(new Material(new Vector3f(0.2f, 0.5f, 0.3f), 0.5f));
        allSpark_02 = new GameEntity(mesh);

        mesh = OBJLoader.loadMesh("/models/REF_ONE_CUBIC_METER.obj");
        mesh.setMaterial(new Material(new Vector3f(0.3f, 0.5f, 0.6f), 0.5f));
        allSpark_03 = new GameEntity(mesh);

        allSpark_01.setPosition(0f, 1f, 0);
        allSpark_02.setPosition(-1f, -1f, 0);
        allSpark_03.setPosition(1f, -1f, 0);

        gameEntities.add(allSpark_01);
        gameEntities.add(allSpark_02);
        gameEntities.add(allSpark_03);

        if(EngineOptions.DEBUG)
        {
            // use this section to create and add gameItem for debug purposes
        }

        scene.setGameItems(gameEntities);
    }

    private void setupLight()
    {
        SceneLight sceneLight = new SceneLight();
        sceneLight.setAmbientLight(new Vector3f(0.2f, 0.2f, 0.2f));

        lightDirection = new Vector3f(0, 1, 1);
        Vector3f lightColor = new Vector3f(1,1,1);
        DirectionalLight directionalLight = new DirectionalLight(lightColor, lightDirection, 1f);

        sceneLight.setDirectionalLight(directionalLight);
        scene.setSceneLight(sceneLight);
    }

    private void setupHUD() throws Exception
    {
        hud = new Hud("");
    }

    private void setupCamera()
    {
        camera.setPosition(0, 10, 10);
        camera.setRotation(45,0,0);
    }

    @Override
    public void input(Window window, MouseInput mouseInput)
    {
        if(EngineOptions.DEBUG)
        {
            totalInputCalls++;
        }

        //QUIT?
        if(KeyboardInput.isKeyReleased(GLFW_KEY_ESCAPE) && EngineOptions.QUIT_ON_ESCAPE)
        {
            glfwSetWindowShouldClose(window.getWindowHandle(), true);
        }

        // Camera controls
        float cameraSpeed = KeyboardInput.isKeyHoldDown(GLFW_KEY_LEFT_SHIFT) ? CAMERA_SPEED_FAST : CAMERA_SPEED;
        cameraIncrement.set(0, 0, 0);

        if (KeyboardInput.isKeyHoldDown(GLFW_KEY_W)) { cameraIncrement.z = -cameraSpeed; }
        else if (KeyboardInput.isKeyHoldDown(GLFW_KEY_S)) { cameraIncrement.z = cameraSpeed; }

        if (KeyboardInput.isKeyHoldDown(GLFW_KEY_A)) { cameraIncrement.x = -cameraSpeed; }
        else if (KeyboardInput.isKeyHoldDown(GLFW_KEY_D)) { cameraIncrement.x = cameraSpeed; }

        if (KeyboardInput.isKeyHoldDown(GLFW_KEY_Q)) { cameraIncrement.y = -cameraSpeed; }
        else if (KeyboardInput.isKeyHoldDown(GLFW_KEY_E)) { cameraIncrement.y = cameraSpeed; }

        // Light
        if(KeyboardInput.isKeyHoldDown(GLFW_KEY_1)) { directionalLightAngle += 1.0f; }
        else if(KeyboardInput.isKeyHoldDown(GLFW_KEY_2)) { directionalLightAngle -= 1.0f;}


    }

    @Override
    public void update(float interval, MouseInput mouseInput)
    {
        if(EngineOptions.DEBUG)
        {
            totalUpdates++;
        }

        updateCamera(mouseInput, interval);
        hud.updateCompass(camera.getRotation().y);
        updateDirectionalLight();

        hud.setStatusText("Welcome, Omnidimensional Creator");

        xRot += 0.1f;
        yRot += 1f;
        zRot += 0.2f;

        allSpark_01.setRotation(xRot,yRot,zRot);
        allSpark_02.setRotation(yRot,zRot,xRot);
        allSpark_03.setRotation(zRot,xRot,yRot);
    }

    private void updateCamera(MouseInput mouseInput, float interval)
    {
        if (mouseInput.isRightButtonPressed())
        {
            Vector2f rotVec = mouseInput.getDisplVec();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
        }

        camera.movePosition(cameraIncrement.x * interval, cameraIncrement.y * interval, cameraIncrement.z * interval);
    }

    private void updateDirectionalLight()
    {
        DirectionalLight directionalLight = scene.getSceneLight().getDirectionalLight();

        float zValue = (float)Math.cos(Math.toRadians(directionalLightAngle));
        float yValue = (float)Math.sin(Math.toRadians(directionalLightAngle));

        lightDirection.x = 0;
        lightDirection.y = yValue;
        lightDirection.z = zValue;
        lightDirection.normalize();

        scene.getSceneLight().getDirectionalLight().setDirection(lightDirection);

        float absDirectionalAngle = Math.abs(directionalLightAngle - 90);

        if (absDirectionalAngle > 90)
        {
            isNight = !isNight;
            if(directionalLightAngle > 180)
            {
                directionalLightAngle = 0;
            }
            else if(directionalLightAngle < 0)
            {
                directionalLightAngle = 180;
            }
        }

        if (absDirectionalAngle > 80f)
        {
            float currentIntensity = 1 - (absDirectionalAngle - 80f) / 10.0f;
            directionalLight.setIntensity(currentIntensity);
        }

        if (absDirectionalAngle > 70f && !isNight)
        {
            float r = 1;
            float g = 1 - (absDirectionalAngle - 70f) / 20.0f;
            float b = 1 - (absDirectionalAngle - 70f) / 10.0f;

            if (g < 0) { g = 0; }
            if (b < 0) { b = 0; }

            directionalLight.getColor().x = r;
            directionalLight.getColor().y = g;
            directionalLight.getColor().z = b;
        }
        else
        {
            if(isNight)
            {
                directionalLight.getColor().x = 0.3f;
                directionalLight.getColor().y = 0.3f;
                directionalLight.getColor().z = 0.5f;
            }
            else
            {
                directionalLight.getColor().x = 1;
                directionalLight.getColor().y = 1;
                directionalLight.getColor().z = 1;
            }
        }
    }

    @Override
    public void render(Window window)
    {
        if(EngineOptions.DEBUG)
        {
            totalRenderCycles++;
        }

        hud.updateSize(window);
        renderer.render(window, camera, scene, hud);
    }

    @Override
    public void cleanup()
    {
        renderer.cleanup();
        scene.cleanup();
        hud.cleanup();

        if(EngineOptions.DEBUG)
        {
            System.out.println("Input Cycles: " + totalInputCalls);
            System.out.println("Update Cycles: " + totalUpdates);
            System.out.println("Render Cycles: " + totalRenderCycles);
        }
    }

}
