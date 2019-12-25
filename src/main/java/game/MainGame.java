package game;

import audio.OpenALAudioEngine;
import core.EngineOptions;
import core.Window;
import hud.Hud;
import hud.TextItem;
import input.KeyboardInput;
import input.MouseInput;
import interfaces.IF_Game;
import interfaces.IF_SceneItem;
import libraries.AudioLibrary;
import org.joml.Vector3f;
import rendering.Camera;
import rendering.Renderer;
import utils.Logger;
import utils.MeshBuilder;
import utils.Utils;

import static org.lwjgl.glfw.GLFW.*;


public class MainGame implements IF_Game {

    private final Renderer renderer;
    private final OpenALAudioEngine audioEngine;

    private Camera camera;
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
    boolean timePaused = true;

    public MainGame(Window _window) {
        renderer =  new Renderer(_window);
        audioEngine = new OpenALAudioEngine();
    }

    @Override
    public void init() throws Exception {
        Logger.getInstance().writeln(">> INITIALISING GAME");
        renderer.init();
        audioEngine.init();
        camera = new Camera(EngineOptions.INITIAL_FOV, 0.20f, 15.0f);
        camera.setPosition(0f,22.5f,45f);
        camera.setRotation(25,0f,0);

        //SET ALL KEYBOARD KEYS TO -1
        KeyboardInput.init();

        //LOAD ASSETS
        AudioLibrary.loadAudioFiles("./res/TestGameContent/Audio.txt");

        //CREATE SCENES
        scenes = new Scene[1];
        scenes[0] = new Scene("./res/TestGameContent/Scenes/MainMenu.scn");

        //CREATE AND ADD OBJECTS TO SCENES
        IF_SceneItem item = new Car(Assets.CTR, Assets.SHADER_DEBUG_TEST);
        scenes[0].addSceneObject("Test_Car", item);

        for(int i = 0; i < 50; i++){
            item = new SimpleObject(MeshBuilder.createTriangle(1+i,3+i), Assets.SHADER_DEBUG_TEST);
            item.getTransform().setPosition(5f,0.5f,i);
            item.setOpacity(0.5f);
            scenes[0].addSceneObject("test_" + i, item);
        }

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
