package game;

import core.Window;
import hud.Hud;
import hud.TextItem;
import input.KeyboardInput;
import core.*;
import input.MouseInput;
import interfaces.IF_Game;
import libraries.AudioLibrary;
import audio.OpenALAudioEngine;
import org.joml.Vector2f;
import org.joml.Vector3f;
import rendering.Camera;
import rendering.Renderer;
import utils.Logger;
import utils.Utils;
import static org.lwjgl.glfw.GLFW.*;


public class Game implements IF_Game {

    private final Renderer renderer;
    private final OpenALAudioEngine audioEngine;

    private Camera camera;
    private Vector3f cameraInc;
    private Scene[] scenes;
    private Scene activeScene = null;
    private Hud hud;

    //IN GAME SETTINGS
    private static final float MOUSE_SENSITIVITY = 0.20f;
    private static final float CAMERA_SPEED = 15.0f;
    private static final float LENGTH_OF_DAY_IN_SECONDS = 3600;
    private float timeOfDay = 0.5f; //from 0.0 to 1.0
    private final Vector3f lightPosition = new Vector3f(10, 10, -25);

    //DEBUG_MODE VALUES
    private float deltaTimeSum;

    public Game(Window _window) {
        renderer =  new Renderer(_window);
        audioEngine = new OpenALAudioEngine();
    }

    @Override
    public void init() throws Exception {
        Logger.getInstance().writeln(">> INITIALISING GAME");
        renderer.init();
        audioEngine.init();
        camera = new Camera(EngineOptions.INITIAL_FOV);
        camera.setPosition(32.3574f,25.414f,59.8928f);
        camera.setRotation(25,-18.1f,0);
        cameraInc = new Vector3f(0,0,0);

        //SET ALL KEYBOARD KEYS TO -1
        KeyboardInput.init();

        //LOAD ASSETS
        AudioLibrary.loadAudioFiles("./res/TestGameContent/Audio.txt");

        //CREATE SCENES
        scenes = new Scene[2];
        scenes[0] = new Scene("./res/TestGameContent/Scenes/MainMenu.scn");
        scenes[1] = new Scene("./res/TestGameContent/Scenes/Scene_01.scn");

        //CREATE AND ADD OBJECTS TO SCENES
        scenes[0].addSceneObject("Logo", new Car(Assets.WHEEL_01, Assets.SHADER_SCENE));
        scenes[1].addSceneObject("Water", new Water(45f, 43f, 60, 60, Assets.SHADER_SCENE_WATER));
        scenes[1].getSceneObjectByTag("Water").getTransform().setPosition(20,-3f,0);
        scenes[1].addSceneObject("Terrain", new Terrain(Assets.SHADER_SCENE));
        scenes[1].addSceneObject("Car_1", new Car(Assets.NSX, Assets.SHADER_SCENE));
        scenes[1].addSceneObject("Car_2", new Car(Assets.GTR, Assets.SHADER_SCENE));
        scenes[1].getSceneObjectByTag("Car_2").getTransform().setPosition(3f,0,-5);

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
        switchScene(1);
    }

    @Override
    public void input(MouseInput mouseInput)
    {
        if(KeyboardInput.isKeyPressedOnce(GLFW_KEY_ESCAPE)) {
            switchScene(0);
        }

        if(KeyboardInput.isKeyPressedOnce(GLFW_KEY_1)) {
            switchScene(1);
        }

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
