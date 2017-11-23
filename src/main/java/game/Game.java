package game;

import Input.KeyboardInput;
import engine.*;
import gameObject.GameObject;
import interfaces.InterfaceGame;
import math.Vector3;
import utils.Logger;
import utils.OBJLoader;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;


public class Game implements InterfaceGame {

    private final OpenGLRenderer renderer;
    private Scene[] scenes;
    private int activeScene;

    private float anim = 0;
    private float rotationSpeed = 10f;
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
        renderer.init();

        int NUMBER_OF_TEST_OBJECTS = 1;
        float OBJECT_GRID_Z_OFFSET = -4f;

        Material sceneMaterial = new Material(new Texture("/textures/car_diffuse.png"), new Texture("/textures/car_normals.png"), new Texture("/textures/car_gloss.png"), null);
        GameObject[] gameObjects = new GameObject[NUMBER_OF_TEST_OBJECTS];

        float x = 0;
        float y = -1;
        float z = OBJECT_GRID_Z_OFFSET;
        Vector3 position = new Vector3(x,y,z);

        for(int i = 0 ; i < NUMBER_OF_TEST_OBJECTS; i++)
        {
            position.set(x, y, z);

            GameObject temp = new GameObject(OBJLoader.loadMesh("/models/cp_deLorean.obj"), sceneMaterial, 2f);
            temp.setPosition(position);

            gameObjects[i] = temp;
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
            anim += rotationSpeed * deltaTime;
        }
        else if(KeyboardInput.isKeyDown(GLFW_KEY_LEFT))
        {
            anim -= rotationSpeed * deltaTime;
        }
    }

    @Override
    public void update(float deltaTime)
    {
        GameObject[] gameObjects = scenes[activeScene].getGameObjects();

        for (GameObject temp : gameObjects)
        {
            if(temp.isVisible())
            {
                temp.setRotation(0, anim, 0);
            }
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
