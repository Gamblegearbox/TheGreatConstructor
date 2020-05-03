package hudStates;

import core.EngineOptions;
import game.Assets;
import game.GameBase;
import game.GameStateManager;
import hud.TextItem;
import input.KeyboardInput;
import interfaces.IF_HudItem;
import interfaces.IF_HudState;
import org.joml.Vector3f;
import utils.Logger;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;

public class EscMenuHud implements IF_HudState {

    private final Map<String, TextItem> hudObjects;
    private final Vector3f markerPosition;

    private final String[] menuCommands;
    private int activeIndex;

    public EscMenuHud() {
        hudObjects = new HashMap<>();
        menuCommands = new String[] {"RESUME", "RESTART", "BACK TO TITLE", "QUIT GAME"};
        activeIndex = 0;
        markerPosition = new Vector3f();
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
        float xStart = EngineOptions.WINDOW_WIDTH / 2.0f - 100;
        float yStart = EngineOptions.WINDOW_HEIGHT / 2.0f - 2f * (fontHeight + gap);

        TextItem text;
        for (String menuCommand : menuCommands) {
            text = new TextItem(menuCommand, Assets.FONT_CONSOLAS, Assets.SHADER_HUD);
            text.getTransform().setPosition(xStart, yStart + hudObjects.size() * (gap + fontHeight), 0f);
            hudObjects.put(menuCommand, text);
        }
        text = new TextItem(">>", Assets.FONT_CONSOLAS, Assets.SHADER_HUD);
        Vector3f firstItemPosition = hudObjects.get(menuCommands[0]).getTransform().getPosition();
        markerPosition.set(firstItemPosition.x - 30, firstItemPosition.y, firstItemPosition.z);
        text.getTransform().setPosition(markerPosition);
        hudObjects.put("marker", text);
    }

    @Override
    public void update(float _deltaTime) {
        if(KeyboardInput.isKeyPressedOnce(GLFW_KEY_DOWN)) {
            if (activeIndex < menuCommands.length - 1) {
                activeIndex++;
            }
            else {
                activeIndex = 0;
            }
            updateMarkerPosition();
        }

        if(KeyboardInput.isKeyPressedOnce(GLFW_KEY_UP)) {
            if (activeIndex > 0) {
                activeIndex--;
                activeIndex = Math.abs(activeIndex % menuCommands.length);
            }
            else {
                activeIndex = menuCommands.length - 1;
            }
            updateMarkerPosition();
        }

        if(KeyboardInput.isKeyPressedOnce(GLFW_KEY_ENTER)){
            switch(menuCommands[activeIndex]){
                case "RESUME":
                    GameStateManager.getInstance().leaveMenu(GameBase.IN_GAME_HUD);
                    break;
                case "RESTART":
                    GameStateManager.getInstance().resetGame();
                    GameStateManager.getInstance().leaveMenu(GameBase.IN_GAME_HUD);
                    GameStateManager.getInstance().changeGameState(GameBase.GAME_SCENE);
                    break;
                case "BACK TO TITLE":
                    GameStateManager.getInstance().resetGame();
                    GameStateManager.getInstance().leaveMenu(GameBase.TITLE_SCREEN_HUD);
                    GameStateManager.getInstance().changeGameState(GameBase.TITLE_SCREEN);
                    break;
                case "QUIT GAME":
                    GameStateManager.getInstance().quitGame();
                    break;
            }
            activeIndex = 0;
        }

    }

    @Override
    public void cleanup() {
        Logger.getInstance().writeln(">>> CLEANING UP " + "HUD");

        for (IF_HudItem temp : hudObjects.values()) {
            temp.cleanup();
        }
    }

    private void updateMarkerPosition(){
        markerPosition.y = hudObjects.get(menuCommands[activeIndex]).getTransform().getPosition().y;
        hudObjects.get("marker").getTransform().getPosition().y = markerPosition.y;
    }
}
