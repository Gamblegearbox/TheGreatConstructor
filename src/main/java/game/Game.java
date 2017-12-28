package game;

import Input.KeyboardInput;
import engine.*;
import gameObject.GameObject;
import interfaces.InterfaceGame;
import math.Vector3;
import utils.Logger;

import static org.lwjgl.glfw.GLFW.*;


public class Game implements InterfaceGame {

    private final OpenGLRenderer renderer;
    private Scene[] scenes;
    private Scene activeScene = null;

    private float anim_X = 0;
    private float anim_Y = 0;
    private float animSpeed = 1f;
    private float time = 0;
    private Material sceneMaterial;

    //DEBUG VALUES
    private float deltaTimeSum;

    public Game(OpenGLRenderer _renderer)
    {
        renderer = _renderer;
    }

    @Override
    public void init() throws Exception
    {
        Logger.getInstance().writeln("> INITIALISING GAME");
        sceneMaterial = new Material(new Texture("/textures/iav.png"), new Texture("/textures/car_normals.png"), new Texture("/textures/cube/gloss.png"), new Texture("/textures/cube/illumination.png"));
        int NUMBER_OF_TEST_OBJECTS = 50;

        scenes = new Scene[2];
        scenes[0] = new Scene("Main Menu", new Vector3(-1f,0.0f,1.0f), NUMBER_OF_TEST_OBJECTS, "/models/REF_ONE_CUBIC_METER.obj", sceneMaterial);
        scenes[1] = new Scene("Test Level", new Vector3(1f,0.0f,0.5f), NUMBER_OF_TEST_OBJECTS, "/models/E_Engine.obj", sceneMaterial);
    }

    public void start() throws Exception
    {
        switchScene(0);
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

        if(KeyboardInput.isKeyDown(GLFW_KEY_L))
        {
            switchScene(1);
        }
    }

    @Override
    public void update(float deltaTime)
    {
        GameObject[] gameObjects = activeScene.getGameObjects();

        for (GameObject temp : gameObjects)
        {
            float x = temp.getPosition().x;
            x += anim_X;
            float y = temp.getPosition().y;
            y += anim_Y;

            temp.setPosition(x, y, temp.getPosition().z);
            temp.setRotation(0, time,0);
        }

        if(EngineOptions.DEBUG)
        {
            deltaTimeSum += deltaTime;

            if( deltaTimeSum > EngineOptions.LOGGING_INTERVAL)
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

    private void switchScene(int _index) throws Exception
    {
        activeScene = scenes[_index];
        activeScene.load();
    }
}
