package game;

import input.KeyboardInput;
import core.*;
import input.MouseInput;
import interfaces.IF_Game;
import libraries.AudioLibrary;
import audio.OpenALAudioEngine;
import org.joml.Vector2f;
import org.joml.Vector3f;
import rendering.DefaultRenderer;
import utils.Logger;

import static org.lwjgl.glfw.GLFW.*;


public class Game implements IF_Game {

    private final DefaultRenderer renderer;
    private final OpenALAudioEngine audioEngine;

    private Camera camera;
    private Vector3f cameraInc;
    private Scene[] scenes;
    private Scene activeScene = null;

    //IN GAME TIME SETTINGS
    private float timeOfDay = 0.5f; //from 0.0 to 1.0
    private float lengthOfDayInSeconds = 60.0f;

    //LIGHT SETTINGS
    private Vector3f lightPosition = new Vector3f(-1.0f, 1, 0);

    //DEBUG_MODE VALUES
    private float deltaTimeSum;

    public Game(Window _window)
    {
        renderer =  new DefaultRenderer(_window);
        audioEngine = new OpenALAudioEngine();
    }

    @Override
    public void init() throws Exception
    {
        Logger.getInstance().writeln(">> INITIALISING GAME");
        renderer.init();
        audioEngine.init();
        camera = new Camera(EngineOptions.INITIAL_FOV);
        camera.setPosition(62.3574f,33.414f,49.8928f);
        camera.setRotation(25,-48.1f,0);
        cameraInc = new Vector3f(0,0,0);

        //SET ALL KEYBOARD KEYS TO -1
        KeyboardInput.init();

        //LOAD ASSETS

        AudioLibrary.loadAudioFiles("./res/TestGameContent/Audio.txt");

        //CREATE SCENES
        scenes = new Scene[2];
        scenes[0] = new Scene("./res/TestGameContent/Scenes/MainMenu.scn");
        scenes[1] = new Scene("./res/TestGameContent/Scenes/Scene_01.scn");

        //CREATE AND ADD OBJECTS TO SCENES
        scenes[0].addSceneObject("Logo", new Logo());
        scenes[1].addSceneObject("Terrain", new Terrain());
        scenes[1].addSceneObject("Water", new Water());
        scenes[1].addSceneObject("Car_1", new Car(Assets.NSX));
        scenes[1].addSceneObject("Car_2", new Car(Assets.GTR));
        scenes[1].getSceneObjectByTag("Car_2").getTransform().setPosition(3f,0,-5);

    }

    public void start()
    {
        switchScene(1);
    }

    @Override
    public void input(MouseInput mouseInput)
    {
        if(KeyboardInput.isKeyPressedOnce(GLFW_KEY_ESCAPE)) {
            switchScene(0);
        }

        if(KeyboardInput.isKeyPressedOnce(GLFW_KEY_1)) {
            switchScene(1);
        }

        if(KeyboardInput.isKeyPressedOnce(GLFW_KEY_2)) {
            renderer.switchShader();
        }

        cameraInc.set(0, 0, 0);
        if (KeyboardInput.isKeyRepeated(GLFW_KEY_W)) {
            cameraInc.z = -1;
        }
        else if (KeyboardInput.isKeyRepeated(GLFW_KEY_S)) {
            cameraInc.z = 1;
        }
        if (KeyboardInput.isKeyRepeated(GLFW_KEY_A)) {
            cameraInc.x = -1;
        }
        else if (KeyboardInput.isKeyRepeated(GLFW_KEY_D)) {
            cameraInc.x = 1;
        }
        if (KeyboardInput.isKeyRepeated(GLFW_KEY_Q)) {
            cameraInc.y = -1;
        }
        else if (KeyboardInput.isKeyRepeated(GLFW_KEY_E)) {
            cameraInc.y = 1;
        }

    }

    @Override
    public void update(float _deltaTime, MouseInput mouseInput)
    {
        //UPDATE IN GAME TIME
        //timeOfDay += 1.0/ lengthOfDayInSeconds * _deltaTime;
        if(timeOfDay > 1.0){
            timeOfDay = 0;
        }

        //UPDATE LIGHT DIRECTION
        //TODO: implement fake sunlight movement

        float MOUSE_SENSITIVITY = 0.20f;
        float CAMERA_SPEED = 15.0f;

        camera.movePosition(cameraInc.x * CAMERA_SPEED * _deltaTime,
                cameraInc.y * CAMERA_SPEED * _deltaTime,
                cameraInc.z * CAMERA_SPEED * _deltaTime);

        // Update camera based on mouse
        if (mouseInput.isRightButtonPressed()) {
            Vector2f rotVec = mouseInput.getDisplVec();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
        }

        activeScene.update(_deltaTime);

        if(EngineOptions.DEBUG_MODE)
        {
            deltaTimeSum += _deltaTime;

            if( deltaTimeSum > EngineOptions.LOGGING_INTERVAL)
            {
                Logger.getInstance().outputLoggedData();
                deltaTimeSum = 0;
            }
        }
    }

    @Override
    public void render()
    {
        renderer.render(activeScene, lightPosition, timeOfDay, camera);
    }

    @Override
    public void cleanup()
    {
        Logger.getInstance().writeln(">> CLEANING UP GAME");
        for(Scene temp : scenes)
        {
            temp.cleanup();
        }

        renderer.cleanup();
        audioEngine.cleanup();
    }

    private void switchScene(int index)
    {
        activeScene = scenes[index];
        Logger.getInstance().writeln("> LOADING " + activeScene.getSceneName() +  "...");
    }
}
