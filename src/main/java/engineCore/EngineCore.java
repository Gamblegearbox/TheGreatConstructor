package engineCore;

import game.Game;
import interfaces.IGame;

public class EngineCore implements Runnable{

    private final Thread gameLoopThread;
    private final Window window;
    private final IGame game;

    public EngineCore()
    {
        gameLoopThread = new Thread(this, "GAME_LOOP_THREAD");
        window = new Window(EngineOptions.TITLE, EngineOptions.WINDOW_WIDTH, EngineOptions.WINDOW_HEIGHT);
        game = new Game();
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
        initSubSystems();
        startGameloop();

        cleanup();
    }

    public void initSubSystems()
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
        System.out.println("...Engine stopped");
    }

}
