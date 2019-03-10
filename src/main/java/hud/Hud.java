package hud;

import interfaces.IF_Hud;
import interfaces.IF_SceneItem;
import utils.Logger;
import java.util.HashMap;
import java.util.Map;

public class Hud implements IF_Hud {

    public static final float LAYOUT_PADDING_X = 10f;
    public static final float LAYOUT_PADDING_Y = 10f;
    public static final float ROW_GAP = 5f;

    private final Map<String, TextItem> hudObjects;

    public Hud(){
        hudObjects = new HashMap<>();
    }

    public void addSceneObject(String tag, TextItem _sceneObject) {
        hudObjects.put(tag, _sceneObject);
    }

    public Map<String, TextItem> getHudItems(){
        return hudObjects;
    }

    @Override
    public void cleanup() {
        Logger.getInstance().writeln(">>> CLEANING UP " + "HUD");

        for (IF_SceneItem temp : hudObjects.values()) {
            temp.cleanup();
        }
    }
}
