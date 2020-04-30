package hudStates;

import core.EngineOptions;
import game.Assets;
import hud.TextItem;
import interfaces.IF_HudItem;
import interfaces.IF_HudState;
import utils.Logger;
import java.util.HashMap;
import java.util.Map;

public class DebugHud implements IF_HudState {

    private final Map<String, TextItem> hudObjects;
    private float deltaTimeSum;

    public DebugHud() {
        hudObjects = new HashMap<>();
        init();
    }

    @Override
    public Map<String, TextItem> getHudItems() {
        return hudObjects;
    }

    @Override
    public TextItem getItemByTag(String _tag) {
        return hudObjects.get(_tag);
    }

    @Override
    public void init() {
        TextItem text = new TextItem("Logging...", Assets.FONT_CONSOLAS, Assets.SHADER_HUD);
        text.getTransform().setPosition(EngineOptions.LAYOUT_PADDING_X,
                                        EngineOptions.WINDOW_HEIGHT - EngineOptions.LAYOUT_PADDING_Y  - Assets.FONT_CONSOLAS.getHeight(), 0f);
        hudObjects.put("LoggedData", text);

        deltaTimeSum = 0.0f;
    }

    @Override
    public void update(float _deltaTime) {
        deltaTimeSum += _deltaTime;

        if( deltaTimeSum > EngineOptions.LOGGING_INTERVAL) {
            hudObjects.get("LoggedData").setText(Logger.getInstance().getAllLoggedData());
            deltaTimeSum = 0;
        }
    }

    @Override
    public void cleanup() {
        Logger.getInstance().writeln(">>> CLEANING UP " + "HUD");

        for (IF_HudItem temp : hudObjects.values()) {
            temp.cleanup();
        }
    }
}
