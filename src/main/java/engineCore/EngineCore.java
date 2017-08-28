package engineCore;

public class EngineCore implements Runnable{

    private final Thread gameLoopThread;
    private final Window window;

    public EngineCore()
    {
        gameLoopThread = new Thread(this, "GAME_LOOP_THREAD");
        window = new Window(engineCore.EngineOptions.TITLE, engineCore.EngineOptions.WINDOW_WIDTH, EngineOptions.WINDOW_WIDTH);
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

    }

    private void update(float deltaTime)
    {
        System.out.println("TIME DELTA: " + deltaTime);
    }

    private void render()
    {
        window.update();
    }

    public void cleanup()
    {
        System.out.println("...cleanup...");
        System.out.println("...Engine stopped");
    }

}
