package game;

import audio.OpenALAudioSource;
import input.KeyboardInput;
import libraries.AudioLibrary;
import libraries.MeshLibrary;
import core.Transform;
import rendering.OpenGLMesh;
import interfaces.IF_SceneObject;

import static org.lwjgl.glfw.GLFW.*;

public class Player implements IF_SceneObject {

    float x = 0;
    float z = 0;
    float y = 0;
    float speed = 15.0f;
    int count = 0;
    int limit = 10000;


    private final int minGear = -1;
    private final int maxGear = 5;

    private boolean isEngineRunning = false;
    private int currentGear = 0;

    Transform transform;
    OpenGLMesh mesh;
    OpenALAudioSource audioEngine;
    OpenALAudioSource audioSecondary;

    public Player(){
        mesh = MeshLibrary.getMeshByTag("Coupe");
        audioEngine = new OpenALAudioSource();
        audioSecondary = new OpenALAudioSource();

        transform = new Transform();
        transform.setPosition(2,-2,-7);
    }

    @Override
    public Transform getTransform(){
        return transform;
    }

    @Override
    public void update(float _deltaTime){

        y += _deltaTime * 9;

        if(isEngineRunning){
            count++;

            if(count % 4000 == 0) {
                audioEngine.play(AudioLibrary.audioBufferIdMap.get("rotary"));
            }

            if(KeyboardInput.isKeyPressedOnce(GLFW_KEY_Q)){
                shutDownEngine();
            }


        } else if(KeyboardInput.isKeyPressedOnce(GLFW_KEY_E)){
            startEngine();
        }


        if(KeyboardInput.isKeyPressedOnce(GLFW_KEY_W)){
            shiftUp();
        }

        if(KeyboardInput.isKeyPressedOnce(GLFW_KEY_S)){
            shiftDown();
        }

        transform.setRotation(0,y, 0);


    }

    private void startEngine(){
        isEngineRunning = true;
        audioEngine.play(AudioLibrary.audioBufferIdMap.get("rotary"));
    }

    private void shutDownEngine(){
        isEngineRunning = false;
        audioEngine.stop();
    }

    private void shiftUp(){
        currentGear++;
        audioSecondary.play(AudioLibrary.audioBufferIdMap.get("gearShift"));

        if(currentGear > maxGear) {
            currentGear = maxGear;
        }
    }

    private void shiftDown(){
        currentGear--;
        audioSecondary.play(AudioLibrary.audioBufferIdMap.get("gearShift"));

        if(currentGear < minGear) {
            currentGear = minGear;
        }
    }

    private void accellerate(){}
    private void brake(){}

    private void steer(){}

    public OpenGLMesh getMesh()
    {
        return mesh;
    }

    public void cleanup()
    {
        mesh.cleanup();
        audioEngine.cleanup();
        audioSecondary.cleanup();
    }


}
