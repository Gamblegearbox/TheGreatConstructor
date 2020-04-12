package abstracts;

import interfaces.IF_SceneItem;

import java.util.Map;

public abstract class GameState {

    public abstract void init();
    public abstract void update(float _deltaTime);
    public abstract void cleanup();

    public abstract Map<String, IF_SceneItem> getSceneItems();
    public abstract IF_SceneItem getItemByTag(String _tag);
}
