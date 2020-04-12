package game;

import audio.OpenALAudioSource;
import input.KeyboardInput;
import interfaces.IF_SceneItem;
import libraries.AudioLibrary;
import org.joml.Vector3f;
import rendering.Mesh;
import rendering.ShaderProgram;
import rendering.Transform;

import static org.lwjgl.glfw.GLFW.*;

public class Explorer implements IF_SceneItem {

    private boolean isEngineRunning = true;
    private float illuminationAmount = 0.0f;
    private float count = 0;
    private final Transform transform;
    private final ShaderProgram shader;
    private final Mesh mesh;
    private float distanceToCamera;
    private float opacity = 1.0f;

    //CAR DATA
    private float accel = 20f;
    private float maxSpeed = 65f;
    private float coast = 20f;

    private float verticalSpeed = 5;


    //CAR STATE
    private float rotY = 0f;
    private float posY = 0f;
    private float speed = 0f;
    private float angularSpeed = 0;

    private Vector3f direction;
    private Vector3f velocity;

    //AUDIO
    private final OpenALAudioSource audioEngine;
    private final OpenALAudioSource audioSecondary;

    public Explorer(Mesh _mesh, ShaderProgram _shader){
        transform = new Transform();
        shader = _shader;
        mesh = _mesh;
        audioEngine = new OpenALAudioSource();
        audioSecondary = new OpenALAudioSource();

        direction = new Vector3f(0,0,-1);
        velocity = new Vector3f(0,0,0);
    }

    @Override
    public void update(float _deltaTime){

        if(isEngineRunning){
            count++;

            if(count % 800 == 0) {
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

        if(KeyboardInput.isKeyRepeated(GLFW_KEY_SPACE)){
            if(speed < maxSpeed) {
                speed += _deltaTime * accel;
            }
        } else {
            speed -= _deltaTime * coast;
            if (speed <= 0.0f){
                speed = 0.0f;
            }
        }

        if(KeyboardInput.isKeyRepeated(GLFW_KEY_UP)){
            posY -= _deltaTime * verticalSpeed;
        }

        if(KeyboardInput.isKeyRepeated(GLFW_KEY_DOWN)) {
            posY += _deltaTime * verticalSpeed;
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
        transform.getPosition().y = posY;
        transform.setRotation(0, rotY,0);
    }

    private void startEngine(){
        isEngineRunning = true;
        audioEngine.play(AudioLibrary.audioBufferIdMap.get("rotary"));
    }

    private void shutDownEngine(){
        isEngineRunning = false;
        audioEngine.stop();
    }

    private void calcAngularSpeed(){
            angularSpeed = 10 * 25 - speed * 2f;
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


    public void cleanup() {
        mesh.cleanup();
        audioEngine.cleanup();
        audioSecondary.cleanup();
    }

}
