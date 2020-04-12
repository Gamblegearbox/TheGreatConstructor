package game;

import audio.OpenALAudioEngine;
import core.EngineOptions;
import core.Window;
import gameStates.MainGameScene;
import gameStates.TitleScreen;
import hudStates.DebugHud;
import hudStates.InGameHud;
import hudStates.TitleScreenHud;
import input.KeyboardInput;
import input.MouseInput;
import interfaces.IF_Game;
import interfaces.IF_GameState;
import interfaces.IF_HudState;
import libraries.AudioLibrary;
import org.joml.Vector3f;
import rendering.Camera;
import rendering.Renderer;
import utils.Logger;
import utils.Utils;

import static org.lwjgl.glfw.GLFW.*;

public class MainGame implements IF_Game {

    private final Renderer renderer;
    private final OpenALAudioEngine audioEngine;
    private final Window window;

    private Camera camera;
    private IF_GameState[] gameStates;
    private IF_GameState activeGameState = null;
    private IF_HudState debugHud;
    private IF_HudState[] hudStates;
    private IF_HudState activeHudState = null;

    //IN GAME
    private static final float LENGTH_OF_DAY_IN_SECONDS = 60;
    private float timeOfDay = 0.5f; //from 0.0 to 1.0
    private final Vector3f lightPosition = new Vector3f(10, 10, 10);

    //TEST VALUES
    boolean timePaused = true;

    public MainGame(Window _window) {
        renderer =  new Renderer(_window);
        audioEngine = new OpenALAudioEngine();
        window = _window;
    }

    @Override
    public void init() throws Exception {
        Logger.getInstance().writeln(">> INITIALISING GAME");
        renderer.init();
        audioEngine.init();
        camera = new Camera(EngineOptions.INITIAL_FOV, 0.20f, 15.0f);
        camera.setPosition(45,45,45);
        camera.setRotation(35,-45,0);

        //SET ALL KEYBOARD KEYS TO -1
        KeyboardInput.init();

        //LOAD ASSETS
        AudioLibrary.loadAudioFiles("./res/TestGameContent/Audio.txt");

        //CREATE GAME STATES
        gameStates = new IF_GameState[2];
        gameStates[0] = new TitleScreen();
        gameStates[1] = new MainGameScene();

        //CREATE HUD STATES
        debugHud = new DebugHud();
        hudStates = new IF_HudState[2];
        hudStates[0] = new TitleScreenHud();
        hudStates[1] = new InGameHud();
        //hudStates[1] = null; //ESC Menu
        //hudStates[2] = null; //GAME OVER MENU
    }

    public void start() {
        switchScene(0);
        switchHud(0);
    }

    @Override
    public void update(float _deltaTime, MouseInput _mouseInput)
    {
        if(KeyboardInput.isKeyPressedOnce(GLFW_KEY_2)) {
            renderer.switchShader();
        }

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

        activeGameState.update(_deltaTime, this);

        //HUD STUFF
        if(EngineOptions.DEBUG_MODE){
            debugHud.update(_deltaTime);
        }
        activeHudState.update(_deltaTime);

        //TMP until data is provided in a better way
        MainGameScene gameScene = (MainGameScene) gameStates[1];
        hudStates[1].getItemByTag("timeOfDay").setText("TIME OF DAY: " + Utils.convertNormalizedFloatToTime(timeOfDay % 1.0f));
        hudStates[1].getItemByTag("distance").setText("DISTANCE: " + gameScene.getDrivenDistance());
        hudStates[1].getItemByTag("lapTime").setText("LAP TIME: " + gameScene.getLapTime());
        hudStates[1].getItemByTag("lastLap").setText("LAST LAP: " + gameScene.getLastLap());
    }

    @Override
    public void render(float _deltaTime) {
        renderer.render(activeGameState, activeHudState, debugHud, camera, lightPosition, timeOfDay % 1.0f, _deltaTime);
    }

    @Override
    public void cleanup() {
        Logger.getInstance().writeln(">> CLEANING UP GAME");
        for(IF_GameState temp : gameStates)
        {
            temp.cleanup();
        }

        renderer.cleanup();
        audioEngine.cleanup();
    }

    public void switchScene(int _index) {
        activeGameState = gameStates[_index];
        Logger.getInstance().writeln("> LOADING " + activeGameState.getClass().getSimpleName() +  "...");
    }
    public void switchHud(int _index) {
        activeHudState = hudStates[_index];
        Logger.getInstance().writeln("> LOADING " + activeHudState.getClass().getSimpleName() +  "...");
    }
    public void quit(){
        glfwSetWindowShouldClose(window.getWindowHandle(),true);
    }
}
