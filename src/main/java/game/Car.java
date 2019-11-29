package game;

import input.KeyboardInput;
import audio.OpenALAudioSource;
import libraries.AudioLibrary;
import rendering.ShaderProgram;
import rendering.Transform;
import rendering.Mesh;
import interfaces.IF_SceneItem;

public class Car implements IF_SceneItem {

    int count = 0;
    int limit = 10000;
    float anim = 0f;

    private final int minGear = -1;
    private final int maxGear = 5;

    private boolean isEngineRunning = false;
    private int currentGear = 0;

    private final Transform transform;
    private final ShaderProgram shader;
    private final Mesh mesh;

    private final OpenALAudioSource audioEngine;
    private final OpenALAudioSource audioSecondary;


    public Car(Mesh _mesh, ShaderProgram _shader){
        transform = new Transform();
        shader = _shader;
        mesh = _mesh;
        audioEngine = new OpenALAudioSource();
        audioSecondary = new OpenALAudioSource();
    }

    @Override
    public Transform getTransform(){
        return transform;
    }

    public Mesh getMesh() {
        return mesh;
    }

    @Override
    public ShaderProgram getShader(){
        return shader;
    }

    @Override
    public float getIllumination() {
        return 0;
    }

    @Override
    public void setIllumination(float _glow) {

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
