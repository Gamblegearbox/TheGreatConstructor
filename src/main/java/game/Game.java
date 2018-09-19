package game;

import Input.KeyboardInput;
import engine.*;
import engine.Transform;
import interfaces.IF_Game;
import utils.Logger;

import static org.lwjgl.glfw.GLFW.*;


public class Game implements IF_Game {

    private final OpenGLRenderer renderer;
    private Scene[] scenes;
    private Scene activeScene = null;;


    Player player;

    //DEBUG_MODE VALUES
    private float deltaTimeSum;

    public Game(OpenGLRenderer _renderer)
    {
        renderer = _renderer;
    }

    @Override
    public void init() throws Exception
    {
        Logger.getInstance().writeln("> INITIALISING GAME");

        MeshLibrary.loadMeshes("/TestGameContent/Scenes/Meshes.txt");
        MaterialLibrary.loadMaterials("/TestGameContent/Scenes/Materials.txt");

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

        if(KeyboardInput.isKeyPressedOnce(GLFW_KEY_SPACE))
        {

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
        renderer.cleanup();

        for(Scene temp : scenes)
        {
            temp.cleanup();
        }

        Logger.getInstance().cleanup();
    }

    private void switchScene(int index) throws Exception
    {
        activeScene = scenes[index];
        Logger.getInstance().writeln("> LOADING " + activeScene.getSceneName() +  "...");
    }
}
