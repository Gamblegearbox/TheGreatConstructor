package engine;

import game.Game;
import interfaces.InterfaceGame;
import utils.Logger;

public class CoreLoop implements Runnable{

    private final Thread gameLoopThread;
    private final Window window;
    private final InterfaceGame game;

    public CoreLoop()
    {
        gameLoopThread = new Thread(this, "GAME_LOOP_THREAD");
        window = new Window(EngineOptions.TITLE, EngineOptions.WINDOW_WIDTH, EngineOptions.WINDOW_HEIGHT);
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
        try {
            initSubSystems();
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

    private void initSubSystems() throws Exception
    {
        window.init();
        game.init();

        if(EngineOptions.DEBUG) { EngineOptions.logAllInfo(); }
    }

    private void startGameLoop() throws Exception
    {
        double lastTime = System.nanoTime() / 1000_000_000.0;
        double deltaTimeSum = 0;
        int frames = 0;
        while(!window.windowShouldClose())
        {
            double currentTime = System.nanoTime() / 1000_000_000.0;
            double deltaTime = currentTime - lastTime;

            if(EngineOptions.DEBUG)
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

            input();
            update((float)deltaTime);
            render();

            lastTime = currentTime;
        }
    }

    private void input()
    {
        game.input();
    }

    private void update(float deltaTime)
    {
        game.update(deltaTime);
    }

    private void render()
    {
        game.render();
        window.update();
    }

    private void cleanup()
    {
        game.cleanup();
    }

}
