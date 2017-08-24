package oldEngineStuff.game;

import oldEngineStuff.engine.core.EngineOptions;
import oldEngineStuff.engine.core.GameEngine;
import oldEngineStuff.engine.interfaces.IGameLogic;


public class Main {

    public static void main(String[] args)
    {
        try
        {
            IGameLogic gameLogic = new Game();
            GameEngine gameEngine = new GameEngine(EngineOptions.TITLE, EngineOptions.WINDOW_WIDTH, EngineOptions.WINDOW_HEIGHT, gameLogic);
            gameEngine.start();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.exit(-1);
        }
    }

}
