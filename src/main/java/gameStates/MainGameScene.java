package gameStates;

import abstracts.GameState;
import game.Assets;
import game.Car;
import game.MainGame;
import game.Terrain;
import input.KeyboardInput;
import interfaces.IF_GameState;
import interfaces.IF_SceneItem;
import utils.Logger;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;

public class MainGameScene implements IF_GameState {

    private final Map<String, IF_SceneItem> sceneObjects;
    private Car playerCar;
    private float lapTime;
    private float lastLap;
    private boolean raceStarted;
    private float drivenDistance;


    public MainGameScene() {
        sceneObjects = new HashMap<>();
        init();
    }

    @Override
    public void init() {
        playerCar = new Car(Assets.CTR, Assets.SHADER_DEBUG_TEST);
        sceneObjects.put("testCar", playerCar );
        sceneObjects.put("terrain", new Terrain(Assets.SHADER_DEBUG_TEST));

        raceStarted = false;
        drivenDistance = 0f;
        lapTime = 0;
        lastLap = 0;
    }

    @Override
    public void update(float _deltaTime, MainGame _game) {
        if(raceStarted){
            lapTime += _deltaTime;
            drivenDistance += playerCar.getCurrentSpeed() * _deltaTime;

            if(drivenDistance > 500){
                raceStarted = false;
                lastLap = lapTime;
                lapTime = 0;
                drivenDistance = 0;
            }
        }
        else {
            if(KeyboardInput.isKeyPressedOnce(GLFW_KEY_UP)) {
                raceStarted = true;
            }
        }

        for(IF_SceneItem temp: sceneObjects.values()) {
            temp.update(_deltaTime);
        }
    }

    @Override
    public void cleanup() {
        Logger.getInstance().writeln(">>> CLEANING UP " + this.getClass(). getSimpleName());
        for (IF_SceneItem temp : sceneObjects.values()) {
            temp.cleanup();
        }
    }

    @Override
    public Map<String, IF_SceneItem> getSceneItems() {
        return sceneObjects;
    }

    @Override
    public IF_SceneItem getItemByTag(String _tag) {
        return sceneObjects.get(_tag);
    }

    public float getLapTime() {
        return lapTime;
    }
    public float getLastLap() {
        return lastLap;
    }
    public float getDrivenDistance() {
        return drivenDistance;
    }
}
