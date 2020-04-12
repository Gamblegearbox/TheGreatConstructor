package interfaces;

import game.MainGame;
import java.util.Map;

public interface IF_GameState {

    void init();
    void update(float _deltaTime, MainGame _game);
    void cleanup();

    Map<String, IF_SceneItem> getSceneItems();
    IF_SceneItem getItemByTag(String _tag);
}
