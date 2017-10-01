package game;

import engine.*;
import gameObject.GameObject;
import interfaces.InterfaceGame;
import math.Vector3f;
import utils.OBJLoader;

public class Game implements InterfaceGame {

    private final OpenGLRenderer renderer;
    //private final Window window;
    private final Vector3f lightPosition;

    private GameObject[] gameObjects;

    private float anim = 0;

    public Game(Window _window)
    {
        renderer = new OpenGLRenderer(_window);
        lightPosition = new Vector3f(-1f,1f,-1f);
    }

    @Override
    public void init() throws Exception
    {
        renderer.init();

        int numberOfTestObjects = 100;
        gameObjects = new GameObject[numberOfTestObjects];
        float x = -3, y = -3, z = -5;
        Vector3f position = new Vector3f(x,y,z);

        for(int i = 0 ; i < numberOfTestObjects; i++)
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

            GameObject temp = new GameObject(OBJLoader.loadMesh("/models/AI_Cars.obj"));
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
    }
}
