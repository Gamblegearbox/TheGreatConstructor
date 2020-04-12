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
        TextItem text = new TextItem("TIME OF DAY: ", Assets.FONT_CONSOLAS, Assets.SHADER_HUD);
        text.getTransform().setPosition(EngineOptions.LAYOUT_PADDING_X, EngineOptions.LAYOUT_PADDING_Y + hudObjects.size() * (EngineOptions.ROW_GAP + Assets.FONT_CONSOLAS.getHeight()),0f);
        hudObjects.put("timeOfDay", text);

        text = new TextItem("DISTANCE: ", Assets.FONT_CONSOLAS, Assets.SHADER_HUD);
        text.getTransform().setPosition(EngineOptions.LAYOUT_PADDING_X, EngineOptions.LAYOUT_PADDING_Y + hudObjects.size() * (EngineOptions.ROW_GAP + Assets.FONT_CONSOLAS.getHeight()),0f);
        hudObjects.put("distance", text);

        text = new TextItem("LAP TIME: ", Assets.FONT_CONSOLAS, Assets.SHADER_HUD);
        text.getTransform().setPosition(EngineOptions.LAYOUT_PADDING_X, EngineOptions.LAYOUT_PADDING_Y + hudObjects.size() * (EngineOptions.ROW_GAP + Assets.FONT_CONSOLAS.getHeight()),0f);
        hudObjects.put("lapTime", text);

        text = new TextItem("LAST LAP: ", Assets.FONT_CONSOLAS, Assets.SHADER_HUD);
        text.getTransform().setPosition(EngineOptions.LAYOUT_PADDING_X, EngineOptions.LAYOUT_PADDING_Y + hudObjects.size() * (EngineOptions.ROW_GAP + Assets.FONT_CONSOLAS.getHeight()),0f);
        hudObjects.put("lastLap", text);
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

