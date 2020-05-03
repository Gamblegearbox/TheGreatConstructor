package game;

import input.KeyboardInput;
import audio.OpenALAudioSource;
import libraries.AudioLibrary;
import org.joml.Vector3f;
import rendering.ShaderProgram;
import rendering.Transform;
import rendering.Mesh;
import interfaces.IF_SceneItem;

import static org.lwjgl.glfw.GLFW.*;

public class Car implements IF_SceneItem {

    private boolean isEngineRunning = false;
    private float illuminationAmount = 0.0f;
    private float count = 0;
    private final Transform transform;
    private final ShaderProgram shader;
    private final Mesh mesh;
    private float distanceToCamera;
    private float opacity = 1.0f;

    //CAR DATA
    private final float acceleration = 20f;
    private final float maxSpeed = 65f;
    private final float coast = 5f;
    private final float brake = 20f;

    //CAR STATE
    private float rotY = 0;
    private float speed = 0;
    private float angularSpeed = 0;
    private final Vector3f direction;
    private final Vector3f velocity;

    //AUDIO
    private final OpenALAudioSource audioEngine;
    private final OpenALAudioSource audioSecondary;

    public Car(Mesh _mesh, ShaderProgram _shader){
        transform = new Transform();
        shader = _shader;
        mesh = _mesh;
        audioEngine = new OpenALAudioSource();
        audioSecondary = new OpenALAudioSource();

        direction = new Vector3f(0,0,-1);
        velocity = new Vector3f(0,0,0);
    }

    public float getCurrentSpeed(){
        return speed;
    }

    @Override
    public Transform getTransform(){
        return transform;
    }

    @Override
    public Mesh getMesh() {
        return mesh;
    }

    @Override
    public ShaderProgram getShader(){
        return shader;
    }

    @Override
    public void setDistanceToCamera(float _distance) {
        distanceToCamera = _distance;
    }

    @Override
    public float getDistanceToCamera() {
        return distanceToCamera;
    }

    @Override
    public float getIlluminationAmount() {
        return illuminationAmount;
    }

    @Override
    public void setOpacity(float _opacity) {
        opacity = _opacity;
    }

    @Override
    public float getOpacity() {
        return opacity;
    }

    @Override
    public void setIlluminationAmount(float _illuminationAmount) {
        illuminationAmount = _illuminationAmount;
    }

    @Override
    public void update(float _deltaTime){

        if(isEngineRunning){
            count++;

            if(count % 4000 == 0) {
                audioEngine.play(AudioLibrary.audioBufferIdMap.get("rotary"));
            }
        }

        if(KeyboardInput.isKeyPressedOnce(GLFW_KEY_L)) {
            if(illuminationAmount < 1){
                illuminationAmount = 1.0f;
            } else {
                illuminationAmount = 0.0f;
            }
        }

        if(KeyboardInput.isKeyRepeated(GLFW_KEY_UP)){
            if(speed < maxSpeed) {
                speed += _deltaTime * acceleration;
            }

        } else {
            speed -= _deltaTime * coast;
            if (speed <= 0.0f){
                speed = 0.0f;
            }
        }

        if(KeyboardInput.isKeyRepeated(GLFW_KEY_DOWN)){
            speed -= _deltaTime * brake;
            if (speed <= 0.0f){
                speed = 0.0f;
            }
        }

        if(KeyboardInput.isKeyRepeated(GLFW_KEY_LEFT)){
            calcAngularSpeed();
            rotY -= _deltaTime * angularSpeed;
        }

        if(KeyboardInput.isKeyRepeated(GLFW_KEY_RIGHT)){
            calcAngularSpeed();
            rotY += _deltaTime * angularSpeed;
        }

        float temp = (float)Math.toRadians(rotY);
        direction.x = (float)Math.sin(temp);
        direction.z = (float)-Math.cos(temp);
        direction.normalize();

        direction.mul(speed * _deltaTime, velocity);
        transform.getPosition().add(velocity);
        transform.setRotation(0, rotY,0);
    }

    public Vector3f getDirection(){
        return direction;
    }

    private void calcAngularSpeed(){
        if(speed > 25){
            angularSpeed = 10 * 25 - speed * 2f;
        } else {
            angularSpeed = speed * 10;
        }
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
        /*currentGear++;
        audioSecondary.play(AudioLibrary.audioBufferIdMap.get("gearShift"));

        if(currentGear > maxGear) {
            currentGear = maxGear;
        }*/
    }

    private void shiftDown(){
       /* currentGear--;
        audioSecondary.play(AudioLibrary.audioBufferIdMap.get("gearShift"));

        if(currentGear < minGear) {
            currentGear = minGear;
        }*/
    }



    @Override
    public void cleanup() {
        mesh.cleanup();
        audioEngine.cleanup();
        audioSecondary.cleanup();
    }

}
