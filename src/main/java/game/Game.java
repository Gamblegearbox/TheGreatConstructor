package game;

import input.KeyboardInput;
import core.*;
import interfaces.IF_Game;
import libraries.AudioLibrary;
import libraries.MaterialLibrary;
import libraries.MeshLibrary;
import rendering.OpenGLRenderer;
import audio.OpenALAudioEngine;
import utils.Logger;

import static org.lwjgl.glfw.GLFW.*;


public class Game implements IF_Game {

    private final OpenGLRenderer renderer;
    private final OpenALAudioEngine audioEngine;
    private Scene[] scenes;
    private Scene activeScene = null;

    Player player;


    //DEBUG_MODE VALUES
    private float deltaTimeSum;
    private int activeShader = 0;

    public Game(OpenGLRenderer _renderer, OpenALAudioEngine _audioEngine)
    {
        renderer = _renderer;
        audioEngine = _audioEngine;
    }

    @Override
    public void init() throws Exception
    {
        Logger.getInstance().writeln(">> INITIALISING GAME");

        //LOAD ASSETS
        MeshLibrary.loadMeshes("/TestGameContent/Meshes.txt");
        MaterialLibrary.loadMaterials("/TestGameContent/Materials.txt");
        AudioLibrary.loadAudioFiles("/TestGameContent/Audio.txt");

        player = new Player();

        //CREATE SCENES
        scenes = new Scene[2];
        scenes[0] = new Scene("/TestGameContent/Scenes/MainMenu.scn", MaterialLibrary.getMaterialByTag("default"));
        scenes[1] = new Scene("/TestGameContent/Scenes/Scene_01.scn", MaterialLibrary.getMaterialByTag("default"));

        //ADD OBJECTS TO SCENES
        scenes[0].addSceneObject("Logo", new Logo());

        scenes[1].addSceneObject("Player", player);
        scenes[1].addSceneObject("Street", new Street());
    }

    public void start() throws Exception
    {
        switchScene(1);
    }

    @Override
    public void input(float deltaTime) throws Exception
    {
        if(KeyboardInput.isKeyPressedOnce(GLFW_KEY_ESCAPE))
        {
            switchScene(0);
        }

        if(KeyboardInput.isKeyPressedOnce(GLFW_KEY_1))
        {
            switchScene(1);
        }

        if(KeyboardInput.isKeyPressedOnce(GLFW_KEY_2))
        {
            renderer.switchShader();
        }

        if(KeyboardInput.isKeyRepeated(GLFW_KEY_SPACE))
        {
            System.out.println("SPACE");
        }


    }

    @Override
    public void update(float _deltaTime)
    {
        activeScene.update(_deltaTime);

        if(EngineOptions.getOptionAsBoolean("DEBUG_MODE"))
        {
            deltaTimeSum += _deltaTime;

            if( deltaTimeSum > EngineOptions.getOptionAsFloat("LOGGING_INTERVAL"))
            {
                Logger.getInstance().outputLoggedData();
                deltaTimeSum = 0;
            }
        }
    }

    @Override
    public void render()
    {
        renderer.render(activeScene);
    }

    @Override
    public void cleanup()
    {
        Logger.getInstance().writeln(">> CLEANING UP GAME");
        for(Scene temp : scenes)
        {
            temp.cleanup();
        }
    }

    private void switchScene(int index) throws Exception
    {
        activeScene = scenes[index];
        Logger.getInstance().writeln("> LOADING " + activeScene.getSceneName() +  "...");
    }
}
