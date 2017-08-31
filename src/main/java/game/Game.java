package game;

import engineCore.Renderer;
import interfaces.IGame;

public class Game implements IGame{

    private Renderer renderer;

    public Game()
    {
        renderer = new Renderer();
    }

    @Override
    public void init()
    {
        renderer.init();
    }

    @Override
    public void input(){}

    @Override
    public void update(float deltaTime){}

    @Override
    public void render()
    {
        renderer.render();
    }

    @Override
    public void cleanup()
    {
        renderer.cleanup();
    }
}
