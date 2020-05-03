package game;

import audio.OpenALAudioEngine;
import core.EngineOptions;
import core.Window;
import gameStates.GameScreen;
import gameStates.TitleScreen;
import hudStates.*;
import input.KeyboardInput;
import input.MouseInput;
import interfaces.IF_Game;
import interfaces.IF_GameState;
import interfaces.IF_HudState;
import libraries.AudioLibrary;
import org.joml.Vector3f;
import rendering.Renderer;
import utils.Logger;
import utils.Utils;

import static org.lwjgl.glfw.GLFW.*;

public class GameBase implements IF_Game {

    private final Renderer renderer;
    private final OpenALAudioEngine audioEngine;
    private final Window window;

    //GLOBAL COMPONENTS
    private float timeOfDay;
    private static final float LENGTH_OF_DAY_IN_SECONDS = 60;
    private final Vector3f lightPosition = new Vector3f();

    private IF_HudState debugHud;
    private IF_HudState[] hudStates;
    private IF_GameState[] gameStates;
    private IF_HudState activeHudState = null;
    private IF_GameState activeGameState = null;

    public static final int TITLE_SCREEN = 0;
    public static final int GAME_SCENE = 1;

    public static final int TITLE_SCREEN_HUD = 0;
    public static final int IN_GAME_HUD = 1;
    public static final int ESC_MENU_HUD = 2;
    public static final int FINISHED_HUD = 3;

    public GameBase(Window _window) {
        renderer =  new Renderer(_window);
        audioEngine = new OpenALAudioEngine();
        window = _window;
        new GameStateManager(this); //TODO: this is a weird way of a giving the game to a manager which is a half ass singleton at the moment ... there might be a better way
    }

    @Override
    public void init() throws Exception {
        Logger.getInstance().writeln(">> INITIALISING GAME");

        renderer.init();
        audioEngine.init();

        //SET ALL KEYBOARD KEYS TO -1
        KeyboardInput.init();

        //LOAD ASSETS
        AudioLibrary.loadAudioFiles("./res/TestGameContent/Audio.txt");

        //CREATE GAME STATES
        gameStates = new IF_GameState[2];
        gameStates[TITLE_SCREEN] = new TitleScreen();
        gameStates[GAME_SCENE] = new GameScreen();

        //CREATE HUD STATES
        debugHud = new DebugHud();
        hudStates = new IF_HudState[4];
        hudStates[TITLE_SCREEN_HUD] = new TitleScreenHud();
        hudStates[IN_GAME_HUD] = new InGameHud();
        hudStates[ESC_MENU_HUD] = new EscMenuHud();
        hudStates[FINISHED_HUD] = new FinishedHud();

        timeOfDay = 0.5f; //from 0.0 to 1.0
        lightPosition.set(10, 10, 10);
    }

    public void start() {
        changeGameState(0);
        changeHudState(0);
    }

    @Override
    public void update(float _engineDeltaTime, MouseInput _mouseInput)
    {
        float timeMod = GameStateManager.getInstance().getTimeModifier();
        float gameDeltaTime = _engineDeltaTime * timeMod;

        if(KeyboardInput.isKeyPressedOnce(GLFW_KEY_2)) {
            renderer.switchShader();
        }

        //camera.update(_engineDeltaTime, _mouseInput); //USE WHEN IMPLEMENTING FREE CAM
        timeOfDay += 1.0 / LENGTH_OF_DAY_IN_SECONDS * gameDeltaTime;

        //UPDATE LIGHT POS
        float timeToPosition = timeOfDay * (float)Math.PI * 2f;
        lightPosition.x = (float)Math.sin(timeToPosition) * 50f;
        lightPosition.y = (float)Math.abs(Math.cos(timeToPosition)) * 50f + 10f;
        lightPosition.z = (float)-Math.cos(timeToPosition) * 50f;

        //UPDATE HUD AND SCENES
        activeGameState.update(_engineDeltaTime, gameDeltaTime);
        if(EngineOptions.DEBUG_MODE){
            debugHud.update(_engineDeltaTime);
        }
        activeHudState.update(_engineDeltaTime);

        //TMP until data is provided in a better way
        GameScreen gameScene = (GameScreen) gameStates[1];
        hudStates[IN_GAME_HUD].getItemByTag("timeOfDay").setText("TIME OF DAY: " + Utils.convertNormalizedFloatToTime(timeOfDay % 1.0f));
        hudStates[IN_GAME_HUD].getItemByTag("distance").setText("DISTANCE: " + gameScene.getDrivenDistance());
        hudStates[IN_GAME_HUD].getItemByTag("lapTime").setText("LAP TIME: " + gameScene.getLapTime());
        hudStates[FINISHED_HUD].getItemByTag("lapTime").setText("YOUR TIME: " + gameScene.getLapTime());
        hudStates[FINISHED_HUD].getItemByTag("distance").setText("DRIVEN DISTANCE: " + gameScene.getDrivenDistance());
    }

    @Override
    public void render(float _deltaTime) {
        renderer.render(activeGameState, activeHudState, debugHud, lightPosition, timeOfDay % 1.0f, _deltaTime);
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

    public void changeGameState(int _index) {
        activeGameState = gameStates[_index];
        Logger.getInstance().writeln("> LOADING " + activeGameState.getClass().getSimpleName() +  "...");
    }
    public void changeHudState(int _index) {
        activeHudState = hudStates[_index];
        Logger.getInstance().writeln("> LOADING " + activeHudState.getClass().getSimpleName() +  "...");
    }
    public void quit(){
        glfwSetWindowShouldClose(window.getWindowHandle(),true);
    }

    public void reset(){
        gameStates = new IF_GameState[2];
        gameStates[TITLE_SCREEN] = new TitleScreen();
        gameStates[GAME_SCENE] = new GameScreen();

        debugHud = new DebugHud();
        hudStates = new IF_HudState[4];
        hudStates[TITLE_SCREEN_HUD] = new TitleScreenHud();
        hudStates[IN_GAME_HUD] = new InGameHud();
        hudStates[ESC_MENU_HUD] = new EscMenuHud();
        hudStates[FINISHED_HUD] = new FinishedHud();

        timeOfDay = 0.5f; //from 0.0 to 1.0
        lightPosition.set(10, 10, 10);
    }
}
