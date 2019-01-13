package game;

import audio.OpenALAudioSource;
import input.KeyboardInput;
import libraries.AudioLibrary;
import core.Transform;
import rendering.OpenGLMesh;
import interfaces.IF_SceneObject;

import static org.lwjgl.glfw.GLFW.*;

public class Car implements IF_SceneObject {

    float x = 0, y = 0f, z = 0f;

    float speed = 5.0f;
    int count = 0;
    int limit = 10000;


    private final int minGear = -1;
    private final int maxGear = 5;

    private boolean isEngineRunning = false;
    private int currentGear = 0;

    private Transform transform;
    private OpenGLMesh mesh;

    private OpenALAudioSource audioEngine;
    private OpenALAudioSource audioSecondary;


    public Car(){
        mesh = Assets.CAR_1;
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

        if(isEngineRunning){
            count++;

            if(count % 4000 == 0) {
                audioEngine.play(AudioLibrary.audioBufferIdMap.get("rotary"));
            }

            if(KeyboardInput.isKeyRepeated(GLFW_KEY_UP)){
                z -= speed *_deltaTime;
            }

            if(KeyboardInput.isKeyRepeated(GLFW_KEY_DOWN)){
                z += speed * _deltaTime;
            }

            if(KeyboardInput.isKeyRepeated(GLFW_KEY_LEFT)){
                x -= speed * _deltaTime;
            }

            if(KeyboardInput.isKeyRepeated(GLFW_KEY_RIGHT)){
                x += speed * _deltaTime;
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

        transform.setPosition(x, y, z);


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
