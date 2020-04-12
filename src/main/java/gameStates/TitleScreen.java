package gameStates;

import game.Assets;
import game.MainGame;
import game.SimpleObject;
import input.KeyboardInput;
import interfaces.IF_GameState;
import interfaces.IF_SceneItem;
import utils.Logger;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

public class TitleScreen implements IF_GameState {

    private final Map<String, IF_SceneItem> sceneObjects;
    private SimpleObject titleCar;
    private float rotationSpeed;

    public TitleScreen() {
        sceneObjects = new HashMap<>();
        init();
    }

    @Override
    public void init() {
        sceneObjects.put("testCar", new SimpleObject(Assets.CTR, Assets.SHADER_DEBUG_TEST) );
        titleCar = (SimpleObject) getItemByTag("testCar");
        rotationSpeed = 10.0f;
    }

    @Override
    public void update(float _deltaTime, MainGame _game){
        for(IF_SceneItem temp: sceneObjects.values()){
            temp.update(_deltaTime);
        }

        titleCar.getTransform().getRotation().y += _deltaTime * rotationSpeed;

        if(KeyboardInput.isKeyPressedOnce(GLFW_KEY_SPACE)) {
            _game.switchHud(1);
            _game.switchScene(1);
        }

        if(KeyboardInput.isKeyPressedOnce(GLFW_KEY_ESCAPE)) {
            _game.quit();
        }
    }

    @Override
    public void cleanup() {
        Logger.getInstance().writeln(">>> CLEANING UP " + this.getClass().getSimpleName());
        for (IF_SceneItem temp : sceneObjects.values()) {
            temp.cleanup();
        }
    }

    @Override
    public Map<String, IF_SceneItem> getSceneItems() {
        return sceneObjects;
    }

    @Override
    public IF_SceneItem getItemByTag(String _tag){
        return sceneObjects.get(_tag);
    }

}
