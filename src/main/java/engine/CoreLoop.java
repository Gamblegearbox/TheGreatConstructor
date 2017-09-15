package engine;

import game.Game;
import interfaces.InterfaceGame;

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
            startGameloop();
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

    public void initSubSystems() throws Exception
    {
        window.init();
        game.init();

        if(EngineOptions.DEBUG) { EngineOptions.printAllInfo(); }
    }

    public void startGameloop()
    {
        double lastTime = System.nanoTime() / 1000_000_000.0;
        while(!window.windowShouldClose())
        {
            double currentTime = System.nanoTime() / 1000_000_000.0;
            double deltaTime = currentTime - lastTime;

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

    public void cleanup()
    {
        game.cleanup();
    }

}
