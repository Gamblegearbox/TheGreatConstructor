package game;

import engine.*;
import gameObject.GameObject;
import interfaces.InterfaceGame;
import math.Vector3;
import utils.Logger;
import utils.OBJLoader;

public class Game implements InterfaceGame {


    private final OpenGLRenderer renderer;
    //private final Window window;
    private final Vector3 lightPosition;

    private GameObject[] gameObjects;
    private float anim = 0;

    //DEBUG VALUES
    private float deltaTimeSum;
    private static final int NUMBER_OF_TEST_OBJECTS = 100;

    public Game(Window _window)
    {
        renderer = new OpenGLRenderer(_window);
        lightPosition = new Vector3(-1f,1f,-1f);
    }

    @Override
    public void init() throws Exception
    {
        renderer.init();


        gameObjects = new GameObject[NUMBER_OF_TEST_OBJECTS];
        float x = -3, y = -3, z = -3;
        Vector3 position = new Vector3(x,y,z);

        for(int i = 0 ; i < NUMBER_OF_TEST_OBJECTS; i++)
        {
            position.set(x, y, z);

            if(x == 3)
            {
                x = -3;
                if(y == 3)
                {
                    y = -3;
                    z -= 1.5f;
                }
                else
                {
                    y += 1.5f;
                }
            }
            else
            {
                x += 1.5f;
            }

            GameObject temp = new GameObject(OBJLoader.loadMesh("/models/AI_Cars.obj"), new Texture("/textures/ash_uvgrid03.png"), 2f);
            temp.setPosition(position);
            temp.setScale(0.2f,0.2f,0.2f);
            gameObjects[i] = temp;
        }

    }

    @Override
    public void input(){}

    @Override
    public void update(float deltaTime)
    {
        for (GameObject temp : gameObjects)
        {
            if(temp.isVisible())
            {
                temp.setRotation(0, anim, 0);
            }
        }

        anim += 50f * deltaTime;

        if(EngineOptions.DEBUG)
        {
            deltaTimeSum += deltaTime;

            if( deltaTimeSum > EngineOptions.LOGGING_INTERVAL)
            {
                Logger.getInstance().outputLoggedData();
                deltaTimeSum = 0;
            }
        }
    }

    @Override
    public void render()
    {
        renderer.render(gameObjects, lightPosition);
    }

    @Override
    public void cleanup()
    {
        renderer.cleanup();

        for(GameObject temp : gameObjects)
        {
            temp.cleanup();
        }

        Logger.getInstance().cleanup();
    }
}
