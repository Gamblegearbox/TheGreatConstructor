package game;

import engine.*;
import gameObject.GameObject;
import interfaces.InterfaceGame;
import org.joml.Matrix4f;


public class Game implements InterfaceGame {

    private final OpenGLRenderer renderer;
    private final Window window;

    private GameObject[] gameObjects;
    private GameObject adam;
    private GameObject eva;
    private GameObject bob;

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
                -0.25f,  0.25f, 0.25f,
                 0.25f,  0.25f, 0.25f,
                -0.25f, -0.25f, 0.25f,
                 0.25f, -0.25f, 0.25f,

                -0.25f,  0.25f, -0.25f,
                 0.25f,  0.25f, -0.25f,
                -0.25f, -0.25f, -0.25f,
                 0.25f, -0.25f, -0.25f
        };

        float[] normals = new float[vertices.length];

        float[] colors = new float[]{
                0.5f, 0.0f, 0.0f,
                0.0f, 0.5f, 0.0f,
                0.0f, 0.0f, 0.5f,
                0.0f, 0.5f, 0.5f,

                0.5f, 0.0f, 0.0f,
                0.0f, 0.5f, 0.0f,
                0.0f, 0.0f, 0.5f,
                0.0f, 0.5f, 0.5f
        };

        float[] uvCoords = new float[vertices.length / 3 * 2];

        int[] indices = new int[]{
                //FRONT
                0, 2 ,1,
                2, 3, 1,

                0, 4 ,2,
                6, 2, 4,

                6, 4, 5,
                7, 6, 5,



        };
        adam = new GameObject(new OpenGLMesh(vertices, normals, colors, uvCoords, indices));
        adam.getPosition().z = -2f;

        bob = new GameObject(new OpenGLMesh(vertices, normals, colors, uvCoords, indices));
        bob.getPosition().x = -0.5f;
        bob.getPosition().z = -3f;

        vertices = new float[]{
                 0.00f,  0.25f, 0f,
                -0.25f, -0.25f, 0f,
                 0.25f, -0.25f, 0f
        };

        normals = new float[vertices.length];

        colors = new float[]{
                0.5f, 0.0f, 0.0f,
                0.0f, 0.5f, 0.0f,
                0.0f, 0.0f, 0.5f
        };

        uvCoords = new float[vertices.length / 3 * 2];

        indices = new int[]{
                0, 1, 2,
        };
        eva = new GameObject(new OpenGLMesh(vertices, normals, colors, uvCoords, indices));
        eva.getPosition().x = 0.5f;
        eva.getPosition().z = -2f;

        gameObjects = new GameObject[]
                {
                        adam, eva, bob
                };
    }

    @Override
    public void input(){}

    @Override
    public void update(float deltaTime)
    {
        adam.setRotation(0, anim, 0);
        anim += 50f * deltaTime;

        //if key???
        //renderer.setRenderMode(EngineOptions.renderMode.SHADED;)
    }

    @Override
    public void render()
    {
        renderer.prepareFrame();

        for(int i = 0; i < gameObjects.length; i++)
        {
            renderer.render(gameObjects[i]);
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
