package gameStates;

import cameras.SimpleCamera;
import core.EngineOptions;
import game.*;
import input.KeyboardInput;
import interfaces.IF_GameState;
import interfaces.IF_SceneItem;
import org.joml.Vector3f;
import utils.Logger;
import utils.MeshBuilder;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;

public class GameScene implements IF_GameState {

    private final Map<String, IF_SceneItem> sceneObjects;
    private Car playerCar;
    private float lapTime;
    private boolean raceStarted;
    private float drivenDistance;
    private final SimpleCamera camera;
    private final Vector3f cameraOffset;

    public GameScene() {
        sceneObjects = new HashMap<>();
        camera = new SimpleCamera(EngineOptions.INITIAL_FOV);
        camera.setPosition(45,45,45);
        camera.setRotation(35,-45,0);
        cameraOffset = new Vector3f(45, 45, 45);
        init();
    }

    @Override
    public void init() {
        float bannerDistanceFromTrackMid = 5.0f;
        float trackLength = 500;
        int bannerRows = 10;
        IF_SceneItem temp;
        playerCar = new Car(Assets.CTR, Assets.SHADER_DEBUG_TEST);
        playerCar.getTransform().setPosition(0,0,5);
        sceneObjects.put("testCar", playerCar );
        temp = new SimpleObject(MeshBuilder.createPlane(20,1f,1,1,0,2), Assets.SHADER_DEBUG_TEST);
        temp.getTransform().setPosition(0,0.1f,0);
        sceneObjects.put("startLine", temp);
        temp = new SimpleObject(MeshBuilder.createPlane(20,1f,1,1,0,2), Assets.SHADER_DEBUG_TEST);
        temp.getTransform().setPosition(0,0.1f,-trackLength);
        sceneObjects.put("finishLine", temp);

        int count = 0;
        for(int i = 0; i <= bannerRows; i++){
            IF_SceneItem banner;
            banner = (i%10 == 9) ? new SimpleObject(Assets.BANNER_NIGHT_LIGHT, Assets.SHADER_DEBUG_TEST) : new SimpleObject(Assets.BANNER, Assets.SHADER_DEBUG_TEST);
            banner.getTransform().setPosition(bannerDistanceFromTrackMid,0,-trackLength / bannerRows * i);
            sceneObjects.put("banner_" + count, banner);

            banner = (i%10 == 9) ? new SimpleObject(Assets.BANNER_NIGHT_LIGHT, Assets.SHADER_DEBUG_TEST) : new SimpleObject(Assets.BANNER, Assets.SHADER_DEBUG_TEST);
            banner.getTransform().setPosition(-bannerDistanceFromTrackMid,0,-trackLength / bannerRows * i);
            sceneObjects.put("banner_" + count + 1, banner);
            count += 2;
        }

        raceStarted = false;
        drivenDistance = 0f;
        lapTime = 0;
    }

    @Override
    public void update(float _engineDeltaTime, float _gameDeltaTime) {
        Vector3f playerPos = playerCar.getTransform().getPosition();
        float camBaseDistance = 45 + playerCar.getCurrentSpeed() * 0.4f;

        cameraOffset.set(camBaseDistance, camBaseDistance, camBaseDistance);
        camera.setPosition(playerPos.x + cameraOffset.x, playerPos.y + cameraOffset.y, playerPos.z + cameraOffset.z);

        if(KeyboardInput.isKeyPressedOnce(GLFW_KEY_ESCAPE)) {
            if (GameStateManager.getInstance().isMenuActive()) {
                GameStateManager.getInstance().leaveMenu(2);
            }
            else {
                GameStateManager.getInstance().enterMenu();
            }
        }

        if(raceStarted){
            lapTime += _gameDeltaTime;
            drivenDistance += playerCar.getCurrentSpeed() * _gameDeltaTime;

            if(playerCar.getTransform().getPosition().z < sceneObjects.get("finishLine").getTransform().getPosition().z){
                raceStarted = false;
                //hudStates[3].getItemByTag("lapTime").setText("YOUR TIME: " + lapTime);
                GameStateManager.getInstance().changeGameState(GameBase.TITLE_SCREEN);
                GameStateManager.getInstance().changeHudState(GameBase.FINISHED_HUD);
            }
        }
        else if(playerCar.getTransform().getPosition().z < sceneObjects.get("startLine").getTransform().getPosition().z) { //TODO: triggerbox here
            raceStarted = true;
        }

        for(IF_SceneItem temp: sceneObjects.values()) {
            temp.update(_gameDeltaTime);
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

    @Override
    public SimpleCamera getCamera(){
        return camera;
    }

    public float getLapTime() {
        return lapTime;
    }
    public float getDrivenDistance() {
        return drivenDistance;
    }

}
