package gameStates;

import cameras.SimpleCamera;
import core.EngineOptions;
import game.Assets;
import game.GameStateManager;
import game.SimpleObject;
import input.KeyboardInput;
import interfaces.IF_GameState;
import interfaces.IF_SceneItem;
import cameras.FreeFlyCamera;
import utils.Logger;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

public class TitleScreen implements IF_GameState {

    private final Map<String, IF_SceneItem> sceneObjects;
    private SimpleObject titleCar;
    private float rotationSpeed;
    private final SimpleCamera camera;

    public TitleScreen() {
        sceneObjects = new HashMap<>();
        camera = new SimpleCamera(EngineOptions.INITIAL_FOV);
        camera.setPosition(15,15,15);
        camera.setRotation(35,-45,0);
        init();
    }

    @Override
    public void init() {
        sceneObjects.put("testCar", new SimpleObject(Assets.CTR, Assets.SHADER_DEBUG_TEST) );
        titleCar = (SimpleObject) getItemByTag("testCar");
        rotationSpeed = 10.0f;
    }

    @Override
    public void update(float _engineDeltaTime, float _gameDeltaTime){
        for(IF_SceneItem temp: sceneObjects.values()){
            temp.update(_engineDeltaTime);
        }

        titleCar.getTransform().getRotation().y += _engineDeltaTime * rotationSpeed;

        if(KeyboardInput.isKeyPressedOnce(GLFW_KEY_SPACE)) {
            GameStateManager.getInstance().changeHudState(1);
            GameStateManager.getInstance().changeGameState(1);
        }

        if(KeyboardInput.isKeyPressedOnce(GLFW_KEY_ESCAPE)) {
            GameStateManager.getInstance().quitGame();
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

    @Override
    public SimpleCamera getCamera(){
        return camera;
    }
}
