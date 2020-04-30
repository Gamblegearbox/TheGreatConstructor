package interfaces;


import cameras.SimpleCamera;

import java.util.Map;

public interface IF_GameState {

    void init();
    void update(float _engineDeltaTime, float _gameDeltaTime);
    SimpleCamera getCamera();
    void cleanup();

    Map<String, IF_SceneItem> getSceneItems();
    IF_SceneItem getItemByTag(String _tag);
}
