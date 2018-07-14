package game;

import Input.KeyboardInput;
import engine.*;
import gameObject.GameObject;
import interfaces.InterfaceGame;
import utils.Logger;

import static org.lwjgl.glfw.GLFW.*;


public class Game implements InterfaceGame {

    private final OpenGLRenderer renderer;
    private Scene[] scenes;
    private Scene activeScene = null;
    private int activeSceneIndex;

    private float anim_X = 0;
    private float anim_Y = 0;
    private float animSpeed = 20f;
    private float time = 0;


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

        scenes = new Scene[3];
        scenes[0] = new Scene("/GameJam1807/Scenes/MainMenu.scn");
        scenes[1] = new Scene("/GameJam1807/Scenes/Scene_01.scn");
        scenes[2] = new Scene("/GameJam1807/Scenes/Scene_02.scn");
    }

    public void start() throws Exception
    {
        activeSceneIndex = 0;
        switchScene();
    }

    @Override
    public void input(float deltaTime) throws Exception
    {
        if(KeyboardInput.isKeyDown(GLFW_KEY_RIGHT))
        {
            anim_X = animSpeed * deltaTime;
        }
        else if(KeyboardInput.isKeyDown(GLFW_KEY_LEFT))
        {
            anim_X = -animSpeed * deltaTime;
        }
        else
        {
            anim_X = 0;
        }

        if(KeyboardInput.isKeyDown(GLFW_KEY_UP))
        {
            anim_Y = animSpeed * deltaTime;
        }
        else if(KeyboardInput.isKeyDown(GLFW_KEY_DOWN))
        {
            anim_Y = -animSpeed * deltaTime;
        }
        else
        {
            anim_Y = 0;
        }


        if(KeyboardInput.isKeyPressedOnce(GLFW_KEY_1))
        {
            activeSceneIndex = 1;
            switchScene();
        }

        if(KeyboardInput.isKeyPressedOnce(GLFW_KEY_2))
        {
            activeSceneIndex = 2;
            switchScene();
        }

        if(KeyboardInput.isKeyPressedOnce(GLFW_KEY_ESCAPE))
        {
            activeSceneIndex = 0;
            switchScene();
        }
    }

    @Override
    public void update(float deltaTime)
    {
        GameObject[] gameObjects = activeScene.getGameObjects();

        if(activeSceneIndex==1){

            GameObject ship = gameObjects[1];
            GameObject blaster = gameObjects[2];
            float x = ship.getPosition().x;

            float y = ship.getPosition().y;

            ship.setPosition(x, y, ship.getPosition().z);
            ship.setRotation(0, time, 0);
            blaster.setPosition(x, y, ship.getPosition().z);
            blaster.setRotation(0, time, 0);
            blaster.setScale(1,1,Math.abs((float)Math.sin(time*20000f)));
        }

        if(activeSceneIndex==2){

            GameObject ship = gameObjects[1];
            GameObject blaster = gameObjects[2];

            float x = ship.getPosition().x;
            x += anim_X;
            float y = ship.getPosition().y;
            y += anim_Y;

            ship.setPosition(x, y, ship.getPosition().z);
            ship.setRotation(90, 0, 0);
            blaster.setPosition(x, y, ship.getPosition().z);
            blaster.setRotation(90, 0, 0);
            blaster.setScale(1,1,Math.abs((float)Math.sin(time*20000f)));

            GameObject enemy = gameObjects[3];
            enemy.setRotation(90, 0, 0);
            GameObject enemy1 = gameObjects[4];
            enemy1.setRotation(90, 0, 0);
            GameObject enemy2 = gameObjects[5];
            enemy2.setRotation(90, 0, 0);

        }
        /*
        for (GameObject temp : gameObjects)
        {
            if(!temp.isStatic()) {
                float x = temp.getPosition().x;
                x += anim_X;
                float y = temp.getPosition().y;
                y += anim_Y;

                temp.setPosition(x, y, temp.getPosition().z);
                temp.setRotation(0, time, 0);
            }
        }*/

        if(EngineOptions.getOptionAsBoolean("DEBUG_MODE"))
        {
            deltaTimeSum += deltaTime;

            if( deltaTimeSum > EngineOptions.getOptionAsFloat("LOGGING_INTERVAL"))
            {
                Logger.getInstance().outputLoggedData();
                deltaTimeSum = 0;
            }
        }

        time += deltaTime * 15;
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

    private void switchScene() throws Exception
    {
        activeScene = scenes[activeSceneIndex];
        activeScene.load();
    }
}
