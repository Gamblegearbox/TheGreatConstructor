package hudStates;

import core.EngineOptions;
import game.Assets;
import game.MainGame;
import hud.TextItem;
import interfaces.IF_HudItem;
import interfaces.IF_HudState;
import utils.Logger;

import java.util.HashMap;
import java.util.Map;

public class TitleScreenHud implements IF_HudState {

    private final Map<String, TextItem> hudObjects;

    public TitleScreenHud() {
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
        TextItem text = new TextItem(">>> PRESS SPACE TO START <<<", Assets.FONT_CONSOLAS, Assets.SHADER_HUD);
        text.getTransform().setPosition((EngineOptions.WINDOW_WIDTH / 2.0f) - 150,
                                        EngineOptions.WINDOW_HEIGHT / 2.0f + 50, 0f);
        hudObjects.put("pressToStart", text);
    }

    @Override
    public void update(float _deltaTime) {
    }

    @Override
    public void cleanup() {
        Logger.getInstance().writeln(">>> CLEANING UP " + "HUD");

        for (IF_HudItem temp : hudObjects.values()) {
            temp.cleanup();
        }
    }
}
