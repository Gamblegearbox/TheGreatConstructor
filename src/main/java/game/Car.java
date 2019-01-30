package game;

import audio.OpenALAudioSource;
import input.KeyboardInput;
import libraries.AudioLibrary;
import core.Transform;
import rendering.OpenGLMesh;
import interfaces.IF_SceneObject;

import static org.lwjgl.glfw.GLFW.*;

public class Car implements IF_SceneObject {

    int count = 0;
    int limit = 10000;
    float anim = 0f;

    private final int minGear = -1;
    private final int maxGear = 5;

    private boolean isEngineRunning = false;
    private int currentGear = 0;

    private Transform transform;
    private OpenGLMesh mesh;

    private OpenALAudioSource audioEngine;
    private OpenALAudioSource audioSecondary;


    public Car(OpenGLMesh _mesh){
        mesh = _mesh;
        audioEngine = new OpenALAudioSource();
        audioSecondary = new OpenALAudioSource();

        transform = new Transform();

    }

    @Override
    public Transform getTransform(){
        return transform;
    }

    public OpenGLMesh getMesh() {
        return mesh;
    }


    @Override
    public void update(float _deltaTime){
/*
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

        */
        anim += 15 * _deltaTime;
        transform.setRotation(0f,anim,0f);

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



    public void cleanup()
    {
        mesh.cleanup();
        audioEngine.cleanup();
        audioSecondary.cleanup();
    }


}
