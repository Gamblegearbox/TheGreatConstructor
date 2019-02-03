package core;

import game.Game;
import input.MouseInput;
import interfaces.IF_Game;
import utils.Logger;

import java.util.Scanner;

public class CoreLoop implements Runnable{

    private final Thread gameLoopThread;
    private final Window window;
    private final MouseInput mouseInput;
    private final IF_Game game;

    public CoreLoop()
    {
        gameLoopThread = new Thread(this, "GAME_LOOP_THREAD");
        window = new Window(EngineOptions.WINDOW_TITLE, EngineOptions.WINDOW_WIDTH, EngineOptions.WINDOW_HEIGHT);
        mouseInput = new MouseInput();
        game = new Game(window);
    }

    public void start()
    {
        String osName = EngineOptions.OPERATING_SYSTEM;

        if(osName.contains("Mac"))
        {
            gameLoopThread.run();
        }
        else
        {
            gameLoopThread.start();
        }
    }

    @Override
    public void run()
    {
        try
        {
            init();
            startGameLoop();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            cleanup();
        }
    }

    private void init() throws Exception
    {
        window.init();
        mouseInput.init(window);
        game.init();
    }

    private void startGameLoop() throws Exception
    {
        Logger.getInstance().writeln("> STARTING GAME LOOP");

        game.start();

        double lastTime = System.nanoTime() / 1000_000_000.0;
        double deltaTimeSum = 0;
        int frames = 0;
        while(!window.windowShouldClose())
        {
            double currentTime = System.nanoTime() / 1000_000_000.0;
            double deltaTime = currentTime - lastTime;

            if(EngineOptions.DEBUG_MODE)
            {
                deltaTimeSum += deltaTime;

                if( deltaTimeSum > 1 )
                {
                    Logger.getInstance().logData("FPS", frames);
                    deltaTimeSum = 0;
                    frames = 0;
                }
                frames++;
            }
            float dt = (float)deltaTime;

            input();
            update(dt);
            render();

            lastTime = currentTime;
        }
    }

    private void input()
    {
        mouseInput.input(window);
        game.input(mouseInput);
    }

    private void update(float deltaTime)
    {
        game.update(deltaTime, mouseInput);
    }

    private void render()
    {
        game.render();
        window.update();
    }

    private void cleanup()
    {
        Logger.getInstance().writeln("> CLEANING UP");
        game.cleanup();
        window.cleanup();
        Logger.getInstance().cleanup();
    }

}
