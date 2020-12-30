package gameStates;

import cameras.SimpleCamera;
import core.EngineOptions;
import game.Assets;
import game.GameStateManager;
import game.SimpleObject;
import input.KeyboardInput;
import interfaces.IF_GameState;
import interfaces.IF_SceneItem;
import utils.Logger;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;

public class ChooseCarScreen implements IF_GameState {

    private final Map<String, IF_SceneItem> sceneObjects;
    private IF_SceneItem titleCar;
    private float rotationSpeed;
    private final SimpleCamera camera;
    private String[] carNames;
    private int index;
    private float carRotation;


    public ChooseCarScreen() {
        sceneObjects = new HashMap<>();
        camera = new SimpleCamera(EngineOptions.INITIAL_FOV);
        camera.setPosition(15,15,15);
        camera.setRotation(35,-45,0);

        init();
    }

    @Override
    public void init() {
        index = 0;
        carRotation = 0;
        rotationSpeed = 10.0f;

        sceneObjects.put("CT_R", new SimpleObject(Assets.CT_R, Assets.SHADER_DEBUG_TEST) );
        sceneObjects.put("NS_X", new SimpleObject(Assets.NS_X, Assets.SHADER_DEBUG_TEST) );
        sceneObjects.put("GT_R", new SimpleObject(Assets.GT_R, Assets.SHADER_DEBUG_TEST) );
        sceneObjects.put("RX_S", new SimpleObject(Assets.RX_S, Assets.SHADER_DEBUG_TEST) );

        for (IF_SceneItem item : sceneObjects.values()){
            item.setOpacity(0.0f);
        }

        carNames = new String[sceneObjects.size()];
        sceneObjects.keySet().toArray(carNames);
        titleCar = sceneObjects.get(carNames[Math.abs(index)]);
        titleCar.setOpacity(1.0f);
    }

    @Override
    public void update(float _engineDeltaTime, float _gameDeltaTime){
        for(IF_SceneItem temp: sceneObjects.values()){
            temp.update(_engineDeltaTime);
        }

        carRotation += rotationSpeed * _engineDeltaTime;
        titleCar.getTransform().getRotation().y = carRotation;

        if(KeyboardInput.isKeyPressedOnce(GLFW_KEY_LEFT)) {
            index--;
            changeCar();
        }

        if(KeyboardInput.isKeyPressedOnce(GLFW_KEY_RIGHT)) {
            index++;
            changeCar();
        }
    }

    private void changeCar(){
        index %= carNames.length;
        String key = carNames[Math.abs(index)];
        titleCar.setOpacity(0.0f);
        titleCar = sceneObjects.get(key);
        titleCar.setOpacity(1.0f);
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
