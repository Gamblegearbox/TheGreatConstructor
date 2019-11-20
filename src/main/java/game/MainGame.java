package game;

import audio.OpenALAudioEngine;
import core.EngineOptions;
import core.Window;
import hud.Hud;
import hud.TextItem;
import input.KeyboardInput;
import input.MouseInput;
import interfaces.IF_Game;
import libraries.AudioLibrary;
import org.joml.Vector2f;
import org.joml.Vector3f;
import rendering.Camera;
import rendering.Renderer;
import utils.Logger;
import utils.Utils;

import static org.lwjgl.glfw.GLFW.*;


public class MainGame implements IF_Game {

    private final Renderer renderer;
    private final OpenALAudioEngine audioEngine;

    private Camera camera;
    private Vector3f cameraInc;
    private Scene[] scenes;
    private Scene activeScene = null;
    private Hud hud;

    //IN GAME SETTINGS
    private static final float MOUSE_SENSITIVITY = 0.10f;
    private static final float CAMERA_SPEED = 5.0f;
    private static final float LENGTH_OF_DAY_IN_SECONDS = 3600;
    private float timeOfDay = 0.5f; //from 0.0 to 1.0
    private final Vector3f lightPosition = new Vector3f(10, 10, 10);

    //DEBUG_MODE VALUES
    private float deltaTimeSum;

    //TEST VALUES
    float rotY = 0.0f;
    float rotX = 0.0f;

    public MainGame(Window _window) {
        renderer =  new Renderer(_window);
        audioEngine = new OpenALAudioEngine();
    }

    @Override
    public void init() throws Exception {
        Logger.getInstance().writeln(">> INITIALISING GAME");
        renderer.init();
        audioEngine.init();
        camera = new Camera(EngineOptions.INITIAL_FOV);
        camera.setPosition(0f,0f,15f);
        camera.setRotation(0,0f,0);
        cameraInc = new Vector3f(0,0,0);

        //SET ALL KEYBOARD KEYS TO -1
        KeyboardInput.init();

        //LOAD ASSETS
        AudioLibrary.loadAudioFiles("./res/TestGameContent/Audio.txt");

        //CREATE SCENES
        scenes = new Scene[1];
        scenes[0] = new Scene("./res/TestGameContent/Scenes/MainMenu.scn");


        //CREATE AND ADD OBJECTS TO SCENES
        scenes[0].addSceneObject("Test_Sphere0", new SimpleObject(Assets.SPHERE, Assets.SHADER_DEBUG_TEST));
        scenes[0].getSceneObjectByTag("Test_Sphere0").getTransform().setPosition(0,2.5f,0f);

        scenes[0].addSceneObject("Test_Car0", new SimpleObject(Assets.NSX, Assets.SHADER_DEBUG_TEST));
        scenes[0].getSceneObjectByTag("Test_Car0").getTransform().setPosition(0,-0.5f,0f);

        scenes[0].addSceneObject("Test_Car1", new SimpleObject(Assets.GTR, Assets.SHADER_DEBUG_TEST));
        scenes[0].getSceneObjectByTag("Test_Car1").getTransform().setPosition(0,-3f,0f);


        //HUD ITEMS
        hud = new Hud();
        TextItem text = new TextItem("Logging...", Assets.FONT_CONSOLAS, Assets.SHADER_HUD);
        text.getTransform().setPosition(Hud.LAYOUT_PADDING_X, Hud.LAYOUT_PADDING_Y, 0f);
        hud.addSceneObject("LoggedData", text);

        text = new TextItem("TIME: ", Assets.FONT_CONSOLAS, Assets.SHADER_HUD);
        text.getTransform().setPosition(Hud.LAYOUT_PADDING_X,Hud.LAYOUT_PADDING_Y + Hud.ROW_GAP + Assets.FONT_CONSOLAS.getHeight(),0f);
        hud.addSceneObject("timeOfDay", text);

        text = new TextItem("LIGHT POS: ", Assets.FONT_CONSOLAS, Assets.SHADER_HUD);
        text.getTransform().setPosition(Hud.LAYOUT_PADDING_X,Hud.LAYOUT_PADDING_Y + 2 * (Hud.ROW_GAP + Assets.FONT_CONSOLAS.getHeight()),0f);
        hud.addSceneObject("lightPos", text);

        text = new TextItem("MOUSE POS: ", Assets.FONT_CONSOLAS, Assets.SHADER_HUD);
        text.getTransform().setPosition(Hud.LAYOUT_PADDING_X,Hud.LAYOUT_PADDING_Y + 3 * (Hud.ROW_GAP + Assets.FONT_CONSOLAS.getHeight()),0f);
        hud.addSceneObject("mousePos", text);
    }

    public void start()
    {
        switchScene(0);
    }

    @Override
    public void input(MouseInput mouseInput)
    {
        if(KeyboardInput.isKeyPressedOnce(GLFW_KEY_2)) {
            renderer.switchShader();
        }

        cameraInc.set(0, 0, 0);
        if (KeyboardInput.isKeyRepeated(GLFW_KEY_W)) {
            cameraInc.z = -1;
        }
        else if (KeyboardInput.isKeyRepeated(GLFW_KEY_S)) {
            cameraInc.z = 1;
        }
        if (KeyboardInput.isKeyRepeated(GLFW_KEY_A)) {
            cameraInc.x = -1;
        }
        else if (KeyboardInput.isKeyRepeated(GLFW_KEY_D)) {
            cameraInc.x = 1;
        }
        if (KeyboardInput.isKeyRepeated(GLFW_KEY_Q)) {
            cameraInc.y = -1;
        }
        else if (KeyboardInput.isKeyRepeated(GLFW_KEY_E)) {
            cameraInc.y = 1;
        }
    }

    @Override
    public void update(float _deltaTime, MouseInput mouseInput)
    {
        //TODO: remove later, just for visuals testing
        if(KeyboardInput.isKeyRepeated(GLFW_KEY_LEFT)){
            rotY -= 40.0f * _deltaTime;
        } else if (KeyboardInput.isKeyRepeated(GLFW_KEY_RIGHT)){
            rotY += 40.0f * _deltaTime;
        }

        if(KeyboardInput.isKeyRepeated(GLFW_KEY_UP)){
            rotX += 40.0f * _deltaTime;
        } else if (KeyboardInput.isKeyRepeated(GLFW_KEY_DOWN)){
            rotX -= 40.0f * _deltaTime;
        }

        scenes[0].getSceneObjectByTag("Test_Car0").getTransform().setRotation(rotX,rotY,0);
        scenes[0].getSceneObjectByTag("Test_Car1").getTransform().setRotation(rotX,rotY,0);
        scenes[0].getSceneObjectByTag("Test_Sphere0").getTransform().setRotation(rotX,rotY,0);

        //UPDATE IN GAME TIME
        timeOfDay += 1.0 / LENGTH_OF_DAY_IN_SECONDS * _deltaTime;
        if(timeOfDay > 1.0){
            timeOfDay = 0;
        }

        camera.movePosition(cameraInc.x * CAMERA_SPEED * _deltaTime,
                cameraInc.y * CAMERA_SPEED * _deltaTime,
                cameraInc.z * CAMERA_SPEED * _deltaTime);

        // Update camera based on mouse
        if (mouseInput.isRightButtonPressed()) {
            Vector2f rotVec = mouseInput.getDisplVec();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
        }

        activeScene.update(_deltaTime);
        hud.getHudItems().get("timeOfDay").setText("TIME: " + Utils.convertNormalizedFloatToTime(timeOfDay));
        hud.getHudItems().get("lightPos").setText("LIGHT POS: "
                + (int)lightPosition.x
                + " | "
                + (int)lightPosition.z);

        hud.getHudItems().get("mousePos").setText("MOUSE POS: "
                + mouseInput.getCurrentPos().x
                + " | "
                + mouseInput.getCurrentPos().y);

        if(EngineOptions.DEBUG_MODE)
        {
            deltaTimeSum += _deltaTime;

            if( deltaTimeSum > EngineOptions.LOGGING_INTERVAL)
            {
                hud.getHudItems().get("LoggedData").setText(Logger.getInstance().getAllLoggedData());
                deltaTimeSum = 0;
            }
        }
    }

    @Override
    public void render(float _deltaTime)
    {
        renderer.render(activeScene, hud, camera, lightPosition, timeOfDay, _deltaTime);
    }

    @Override
    public void cleanup()
    {
        Logger.getInstance().writeln(">> CLEANING UP GAME");
        for(Scene temp : scenes)
        {
            temp.cleanup();
        }

        renderer.cleanup();
        audioEngine.cleanup();
    }

    private void switchScene(int index)
    {
        activeScene = scenes[index];
        Logger.getInstance().writeln("> LOADING " + activeScene.getSceneName() +  "...");
    }
}
