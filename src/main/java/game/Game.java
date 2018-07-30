package game;

import Input.KeyboardInput;
import engine.*;
import engine.GameObject;
import interfaces.IF_Game;
import interfaces.IF_GameObjectBehaviour;
import utils.Logger;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;


public class Game implements IF_Game {

    private final OpenGLRenderer renderer;
    private Scene[] scenes;
    private Scene activeScene = null;;

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

        MeshLibrary.loadMeshes("/GameJam1807/Scenes/Meshes.txt");
        MaterialLibrary.loadMaterials("/GameJam1807/Scenes/Materials.txt");

        scenes = new Scene[2];
        scenes[0] = new Scene("/GameJam1807/Scenes/MainMenu.scn", MaterialLibrary.getMaterialByTag("default"));
        GameObject temp = new GameObject(MeshLibrary.getMeshByTag("Logo"), new LogoBehaviour(), false);
        temp.setPosition(0,0, -15);
        scenes[0].addGameObject("logo", temp);

        scenes[1] = new Scene("/GameJam1807/Scenes/Scene_01.scn", MaterialLibrary.getMaterialByTag("default"));
        temp = new GameObject(MeshLibrary.getMeshByTag("Logo"), new NoBehaviour(), false);
        temp.setPosition(-15,-12,-25);
        scenes[1].addGameObject("logo", temp);

        temp = new GameObject(MeshLibrary.getMeshByTag("ship"), new PlayerBehaviour(), false);
        temp.setPosition(15,0,-25);
        scenes[1].addGameObject("ship", temp);

        temp = new GameObject(MeshLibrary.getMeshByTag("shipFlame"), new NoBehaviour(), false);
        temp.setPosition(15,0,-25);
        scenes[1].addGameObject("shipFlame", temp);

        temp = new GameObject(MeshLibrary.getMeshByTag("enemy_1"), new EnemyBehaviour(),false);
        temp.setPosition(-15,0,-25);
        scenes[1].addGameObject("enemy_1", temp);
    }

    public void start() throws Exception
    {
        switchScene(0);
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
