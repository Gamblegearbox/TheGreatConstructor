package engine;

import game.Game;
import interfaces.IF_Game;
import utils.Logger;

public class CoreLoop implements Runnable{

    private final Thread gameLoopThread;
    private final Window window;
    private final OpenGLRenderer renderer;
    private final IF_Game game;

    public CoreLoop()
    {
        gameLoopThread = new Thread(this, "GAME_LOOP_THREAD");
        window = new Window(EngineOptions.WINDOW_TITLE, EngineOptions.getOptionAsInt("WINDOW_WIDTH"), EngineOptions.getOptionAsInt("WINDOW_HEIGHT"));
        renderer =  new OpenGLRenderer(window);
        game = new Game(renderer);
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
        Logger.getInstance().writeln("> INITIALISING SUBSYSTEMS");
        window.init();
        renderer.init();
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

            if(EngineOptions.getOptionAsBoolean("DEBUG_MODE"))
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

            input(dt);
            update(dt);
            render();

            lastTime = currentTime;
        }
    }

    private void input(float deltaTime) throws Exception
    {
        game.input(deltaTime);
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
