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
    private static final float LENGTH_OF_DAY_IN_SECONDS = 60;
    private float timeOfDay = 0.5f; //from 0.0 to 1.0
    private final Vector3f lightPosition = new Vector3f(10, 10, 10);

    //DEBUG_MODE VALUES
    private float deltaTimeSum;

    //TEST VALUES
    boolean timePaused = false;

    public MainGame(Window _window) {
        renderer =  new Renderer(_window);
        audioEngine = new OpenALAudioEngine();
    }

    @Override
    public void init() throws Exception {
        Logger.getInstance().writeln(">> INITIALISING GAME");
        renderer.init();
        audioEngine.init();
        camera = new Camera(EngineOptions.INITIAL_FOV, 0.10f, 5.0f);
        camera.setPosition(0f,22.5f,45f);
        camera.setRotation(25,0f,0);

        cameraInc = new Vector3f(0,0,0);

        //SET ALL KEYBOARD KEYS TO -1
        KeyboardInput.init();

        //LOAD ASSETS
        AudioLibrary.loadAudioFiles("./res/TestGameContent/Audio.txt");

        //CREATE SCENES
        scenes = new Scene[1];
        scenes[0] = new Scene("./res/TestGameContent/Scenes/MainMenu.scn");

        //CREATE AND ADD OBJECTS TO SCENES
        scenes[0].addSceneObject("Test_Car", new Car(Assets.CTR, Assets.SHADER_DEBUG_TEST));
        scenes[0].getSceneObjectByTag("Test_Car").getTransform().setPosition(0, 0f, 0);


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
    public void update(float _deltaTime, MouseInput _mouseInput)
    {
        if(KeyboardInput.isKeyPressedOnce(GLFW_KEY_2)) {
            renderer.switchShader();
        }

        //updateCamera(_deltaTime, _mouseInput);
        camera.update(_deltaTime, _mouseInput);
        //UPDATE IN GAME TIME
        if(KeyboardInput.isKeyPressedOnce(GLFW_KEY_3)) {
            timePaused = !timePaused;
        }

        if(!timePaused) {
            timeOfDay += 1.0 / LENGTH_OF_DAY_IN_SECONDS * _deltaTime;
        }

        //UPDATE LIGHT POS
        float timeToPosition = timeOfDay * (float)Math.PI * 2f;
        lightPosition.x = (float)Math.sin(timeToPosition) * 50f;
        lightPosition.y = (float)Math.abs(Math.cos(timeToPosition)) * 50f + 10f;
        lightPosition.z = (float)-Math.cos(timeToPosition) * 50f;

        activeScene.update(_deltaTime);
        hud.getHudItems().get("timeOfDay").setText("TIME: " + Utils.convertNormalizedFloatToTime(timeOfDay % 1.0f));
        hud.getHudItems().get("lightPos").setText("LIGHT POS: "
                + (int)lightPosition.x
                + " | "
                + (int)lightPosition.z);

        hud.getHudItems().get("mousePos").setText("MOUSE POS: "
                + _mouseInput.getCurrentPos().x
                + " | "
                + _mouseInput.getCurrentPos().y);


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

    private void updateCamera(float _deltaTime, MouseInput _mouseInput) {
        //TODO: implement target cam with position interpolation
        //TODO: put code into camera
        
        // POSITION
        /*
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

        camera.movePosition(cameraInc.x * CAMERA_SPEED * _deltaTime,
                cameraInc.y * CAMERA_SPEED * _deltaTime,
                cameraInc.z * CAMERA_SPEED * _deltaTime);


        // ROTATION
        if (_mouseInput.isRightButtonPressed()) {
            Vector2f rotVec = _mouseInput.getDisplVec();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
        }*/
    }

    @Override
    public void render(float _deltaTime)
    {
        renderer.render(activeScene, hud, camera, lightPosition, timeOfDay % 1.0f, _deltaTime);
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
