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

    float shotX_1 = 0;
    float shotY_1 = 0;

    float shotX_2 = 0;
    float shotY_2 = 0;

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
        float menuCameraOffset = -25f;
        float gameCameraOffset = -65;

        GameObject[] gameObjects = activeScene.getGameObjects();

        if(activeSceneIndex==0) {
            GameObject logo = gameObjects[0];
            logo.setPosition(0, 0, -15);
        }

        if(activeSceneIndex==1){
            GameObject logo = gameObjects[0];
            GameObject ship = gameObjects[1];
            GameObject blaster = gameObjects[2];

            logo.setPosition(-15, -12, menuCameraOffset);
            ship.setPosition(0, 0, menuCameraOffset);
            ship.setRotation(0, time, 0);
            blaster.setPosition(0, 0, menuCameraOffset);
            blaster.setRotation(0, time, 0);
            blaster.setScale(1,1,Math.abs((float)Math.sin(time*20000f)));
        }

        if(activeSceneIndex==2){
            GameObject logo = gameObjects[0];
            GameObject ship = gameObjects[1];
            GameObject blaster = gameObjects[2];
            GameObject shot_1 = gameObjects[gameObjects.length-1];
            GameObject shot_2 = gameObjects[gameObjects.length-2];

            logo.setPosition(-15, -12, menuCameraOffset);

            if(shot_1.getPosition().x > ship.getPosition().x + 100f){
                shotX_1 = ship.getPosition().x + 10f;
                shotY_1 = ship.getPosition().y - 1.8f;
            }
            else{
                shotX_1 += deltaTime*250;
            }

            if(shot_2.getPosition().x > ship.getPosition().x + 75f){
                shotX_2 = ship.getPosition().x + 10f;
                shotY_2 = ship.getPosition().y - 1.8f;
            }
            else{
                shotX_2 += deltaTime*250;
            }


            float x = ship.getPosition().x;
            x += anim_X;
            float y = ship.getPosition().y;
            y += anim_Y;

            ship.setPosition(x, y, gameCameraOffset);
            ship.setRotation(0, -90, 0);
            blaster.setPosition(x, y, gameCameraOffset);
            blaster.setRotation(0, -90, 0);
            blaster.setScale(1,1,Math.abs((float)Math.sin(time*20000f)));

            shot_1.setPosition(shotX_1, shotY_1, gameCameraOffset);
            shot_1.setRotation(0, -90, time*10);
            shot_1.setScale(1,(float)Math.sin(time*200f),3*Math.abs((float)Math.sin(time*200f)));

            shot_2.setPosition(shotX_2, shotY_2, gameCameraOffset);
            shot_2.setRotation(0, -90, time*10);
            shot_2.setScale(1,(float)Math.sin(time*200f),3*Math.abs((float)Math.sin(time*200f)));

            GameObject enemy = gameObjects[3];
            enemy.setPosition(25, 15, gameCameraOffset);
            enemy.setRotation(0, 90, 0);


        }


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
