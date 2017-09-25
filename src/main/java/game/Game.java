package game;

import engine.*;
import gameObject.GameObject;
import interfaces.InterfaceGame;
import org.joml.Matrix4f;
import org.lwjgl.system.CallbackI;
import utils.OBJLoader;
import utils.PrototypMeshes;
import utils.Utils;


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

        adam = new GameObject(OBJLoader.loadMesh("/models/AI_Cars.obj"));
        adam.getPosition().z = -7f;

        bob = new GameObject(PrototypMeshes.cube());
        bob.getPosition().x = -3f;
        bob.getPosition().z = -5f;

        eva = new GameObject(PrototypMeshes.triangle());
        eva.getPosition().x = 1f;
        eva.getPosition().z = -4f;

        gameObjects = new GameObject[]{
            adam,
            eva,
            bob
        };
    }

    @Override
    public void input(){}

    @Override
    public void update(float deltaTime)
    {
        adam.setRotation(0, 45, 0);
        adam.getPosition().y =-2;//(float)Math.sin(anim*0.050);

        bob.setRotation(0, anim, 0);

        anim += 50f * deltaTime;
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
