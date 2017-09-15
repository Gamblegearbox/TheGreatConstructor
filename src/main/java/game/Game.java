package game;

import engine.*;
import gameObject.GameObject;
import interfaces.InterfaceGame;


public class Game implements InterfaceGame {

    private final OpenGLRenderer renderer;
    private final Window window;

    private GameObject[] gameObjects;
    private GameObject adam;
    private GameObject eva;

    private float anim = 0;

    public Game(Window _window)
    {
        window = _window;
        renderer = new OpenGLRenderer(window);
    }

    @Override
    public void init() throws Exception
    {
        renderer.init();

        float[] vertices = new float[]{
                -0.75f,  0.25f, 0.0f,
                -0.25f,  0.25f, 0.0f,
                -0.75f, -0.25f, 0.0f,
                -0.25f, -0.25f, 0.0f
        };

        int[] indices = new int[]{
                0, 2 ,1,
                2, 3, 1,
        };
        adam = new GameObject(new OpenGLMesh(vertices, indices));

        vertices = new float[]{
                0.5f,  0.25f, 0.0f,
                0.25f, -0.25f, 0.0f,
                0.75f, -0.25f, 0.0f
        };

        indices = new int[]{
                0, 1, 2,
        };
        eva = new GameObject(new OpenGLMesh(vertices, indices));

        gameObjects = new GameObject[]
                {
                        adam, eva
                };
    }

    @Override
    public void input(){}

    @Override
    public void update(float deltaTime)
    {
        adam.getPosition().x = (float)Math.sin(anim);
        anim += deltaTime;
    }

    @Override
    public void render()
    {
        renderer.prepareFrame();

        for(int i = 0; i < gameObjects.length; i++)
        {
            renderer.render(gameObjects[i], EngineOptions.renderMode.WIREFRAME_OVERLAY);
        }

        renderer.afterFrame();
    }

    @Override
    public void cleanup()
    {
        renderer.cleanup();

        for(int i = 0; i < gameObjects.length; i++)
        {
            gameObjects[i].cleanup();
        }
    }
}
