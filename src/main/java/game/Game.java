package game;

import Input.KeyboardInput;
import engine.*;
import engine.MeshAndTransform;
import interfaces.IF_Game;
import utils.Logger;

import static org.lwjgl.glfw.GLFW.*;


public class Game implements IF_Game {

    private final OpenGLRenderer renderer;
    private Scene[] scenes;
    private Scene activeScene = null;;


    Player player;
    Enemy enemy;
    Projectile projectile;

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

        player = new Player(new MeshAndTransform(MeshLibrary.getMeshByTag("Ship")));
        player.getMeshAndTransform().setPosition(-15,0,-25);
        player.getMeshAndTransform().setRotation(0,-90,0);

        enemy = new Enemy(new MeshAndTransform(MeshLibrary.getMeshByTag("Enemy_1")));
        enemy.getMeshAndTransform().setPosition(15,0,-25);

        projectile = new Projectile(new MeshAndTransform(MeshLibrary.getMeshByTag("Projectile_1")));

        scenes = new Scene[2];
        scenes[0] = new Scene("/TestGameContent/Scenes/MainMenu.scn", MaterialLibrary.getMaterialByTag("default"));
        scenes[1] = new Scene("/TestGameContent/Scenes/Scene_01.scn", MaterialLibrary.getMaterialByTag("default"));


        scenes[0].addSceneObject("Logo", new Logo(new MeshAndTransform(MeshLibrary.getMeshByTag("Logo"))));
        scenes[1].addSceneObject("Player", player);
        scenes[1].addSceneObject("Enemy", enemy);
        scenes[1].addSceneObject("Shot1", projectile);

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

        if(KeyboardInput.isKeyPressedOnce(GLFW_KEY_SPACE))
        {
            projectile.start(player.getMeshAndTransform().getPosition());
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
