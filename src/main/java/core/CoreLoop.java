package core;

import game.Game;
import input.MouseInput;
import interfaces.IF_Game;
import utils.Logger;

public class CoreLoop implements Runnable{

    private final Thread gameLoopThread;
    private final Thread consoleThread;

    private final Console console;
    private final Window window;
    private final MouseInput mouseInput;
    private final IF_Game game;

    public CoreLoop()
    {
        window = new Window(EngineOptions.WINDOW_TITLE, EngineOptions.WINDOW_WIDTH, EngineOptions.WINDOW_HEIGHT);
        console = new Console();
        mouseInput = new MouseInput();
        game = new Game(window);

        gameLoopThread = new Thread(this, "GAME_LOOP_THREAD");
        consoleThread = new Thread(console, "CONSOLE");

    }

    public void start()
    {
        String osName = EngineOptions.OPERATING_SYSTEM;

        if(osName.contains("Mac"))
        {
            gameLoopThread.run();
            //consoleThread.run();
        }
        else
        {
            gameLoopThread.start();
            //consoleThread.start();
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
            render(dt);

            lastTime = currentTime;
        }
    }

    private void input()
    {
        mouseInput.input(window);
        game.input(mouseInput);
    }

    private void update(float _deltaTime)
    {
        game.update(_deltaTime, mouseInput);
    }

    private void render(float _deltaTime)
    {
        game.render(_deltaTime);
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
