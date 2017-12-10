package game;

import Input.KeyboardInput;
import engine.*;
import gameObject.GameObject;
import interfaces.InterfaceGame;
import math.Vector3;
import utils.Logger;
import utils.OBJLoader;

import static org.lwjgl.glfw.GLFW.*;


public class Game implements InterfaceGame {

    private final OpenGLRenderer renderer;
    private Scene[] scenes;
    private int activeScene;

    private float anim_X = 0;
    private float anim_Y = 0;
    private float animSpeed = 1f;
    private float time = 0;

    //DEBUG VALUES
    private float deltaTimeSum;

    public Game(Window _window)
    {
        renderer = new OpenGLRenderer(_window);
        activeScene = 0;
    }

    @Override
    public void init() throws Exception
    {
        Logger.getInstance().writeTolog("> INITIALISING GAME\n");
        renderer.init();

        int NUMBER_OF_TEST_OBJECTS = 1;
        float OBJECT_GRID_Z_OFFSET = -3f;

        Material sceneMaterial = new Material(new Texture("/textures/car_diffuse.png"), new Texture("/textures/car_normals.png"), new Texture("/textures/cube/gloss.png"), new Texture("/textures/cube/illumination.png"));
        GameObject[] gameObjects = new GameObject[NUMBER_OF_TEST_OBJECTS];

        float x = 0;
        float y = -0.5f;
        float z = OBJECT_GRID_Z_OFFSET;
        Vector3 position = new Vector3(x,y,z);

        for(int i = 0 ; i < NUMBER_OF_TEST_OBJECTS; i++)
        {
            position.set(x, y, z);

            GameObject temp = new GameObject(OBJLoader.loadMesh("/models/E_Engine.obj"), sceneMaterial, 2f);
            temp.setPosition(position);

            gameObjects[i] = temp;

            x+=1f;
        }

        scenes = new Scene[1];
        scenes[0] = new Scene( new Vector3(-1f,0.0f,1.0f), gameObjects );

        activeScene = 0;

    }

    @Override
    public void input(float deltaTime)
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
    }

    @Override
    public void update(float deltaTime)
    {
        GameObject[] gameObjects = scenes[activeScene].getGameObjects();

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
        renderer.render(scenes[activeScene]);
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
}
