package engine.interfaces;

import engine.gameEntities.GameEntity;

public interface IHud {

    GameEntity[] getGameEntities();

    default void cleanup()
    {
        GameEntity[] gameEntities = getGameEntities();

        for(GameEntity gameEntity : gameEntities)
        {
            gameEntity.getMesh().cleanUp();
        }
    }
}
