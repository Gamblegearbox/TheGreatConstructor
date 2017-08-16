package engine.core;

import engine.input.MouseInput;
import engine.interfaces.IGameLogic;

public class GameEngine implements Runnable{

    private final Window window;
    private final Thread gameLoopThread;
    private final Timer timer;
    private final IGameLogic gameLogic;
    private final MouseInput mouseInput;

    public GameEngine(String windowTitle, int width, int height, IGameLogic gameLogic) throws Exception
    {
        gameLoopThread = new Thread(this, "GAME_LOOP_THREAD");
        window = new Window(windowTitle, width, height);
        mouseInput = new MouseInput();
        this.gameLogic = gameLogic;
        timer = new Timer();
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
            gameLoop();
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

    protected void init() throws Exception
    {
        window.init();
        timer.init();
        mouseInput.init(window);
        gameLogic.init(window);

        if(EngineOptions.DEBUG) { EngineOptions.printAllInfo(); }
    }

    protected void gameLoop()
    {
        float elapsedTime;
        float accumulator = 0f;
        float interval = 1f / EngineOptions.TARGET_UPS;

        while(!window.windowShouldClose())
        {
            elapsedTime = timer.getElapsedTime();
            accumulator += elapsedTime;

            input();
            while(accumulator >= interval)
            {
                update(interval);
                accumulator -= interval;
            }

            render();

            if(!EngineOptions.V_SYNC)
            {
                sync();
            }
        }
    }

    protected void cleanup()
    {
        gameLogic.cleanup();
    }

    private void sync()
    {
        float loopSlot = 1f / EngineOptions.TARGET_FPS;
        double endTime = timer.getLastLoopTime() + loopSlot;

        while(timer.getTime() < endTime)
        {
            try
            {
                Thread.sleep(1);
            }
            catch(InterruptedException ie)
            {
                ie.printStackTrace();
            }
        }
    }

    protected void input()
    {
        mouseInput.input(window);
        gameLogic.input(mouseInput);
    }

    protected void update(float interval)
    {
        gameLogic.update(interval, mouseInput);
    }

    protected void render()
    {
        gameLogic.render();
        window.update();
    }
}