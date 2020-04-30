package hudStates;

import core.EngineOptions;
import game.Assets;
import hud.TextItem;
import interfaces.IF_HudItem;
import utils.Logger;
import java.util.HashMap;
import java.util.Map;

public class InGameHud implements interfaces.IF_HudState {

    private final Map<String, TextItem> hudObjects;

    public InGameHud() {
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
        float fontHeight = Assets.FONT_CONSOLAS.getHeight();
        float gap = EngineOptions.ROW_GAP;
        float xStart = EngineOptions.LAYOUT_PADDING_X;
        float yStart = EngineOptions.LAYOUT_PADDING_Y;

        TextItem text = new TextItem("TIME OF DAY: ", Assets.FONT_CONSOLAS, Assets.SHADER_HUD);
        text.getTransform().setPosition(xStart, yStart + hudObjects.size() * (gap + fontHeight),0f);
        hudObjects.put("timeOfDay", text);

        text = new TextItem("DISTANCE: ", Assets.FONT_CONSOLAS, Assets.SHADER_HUD);
        text.getTransform().setPosition(xStart, yStart + hudObjects.size() * (gap + fontHeight),0f);
        hudObjects.put("distance", text);

        text = new TextItem("LAP TIME: ", Assets.FONT_CONSOLAS, Assets.SHADER_HUD);
        text.getTransform().setPosition(xStart, yStart + hudObjects.size() * (gap + fontHeight),0f);
        hudObjects.put("lapTime", text);
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

