package game;

import audio.OpenALAudioSource;
import libraries.AudioLibrary;
import libraries.MeshLibrary;
import core.Transform;
import rendering.OpenGLMesh;
import interfaces.IF_SceneObject;

public class Player implements IF_SceneObject {

    float x = 0;
    float z = 0;
    float y = 0;
    float speed = 15.0f;
    int count = 0;

    Transform transform;
    OpenGLMesh mesh;
    OpenALAudioSource audioEngine;
    OpenALAudioSource audioCrack;

    public Player(){
        mesh = MeshLibrary.getMeshByTag("Coupe");
        audioEngine = new OpenALAudioSource();
        audioCrack = new OpenALAudioSource();

        transform = new Transform();
        transform.setPosition(2,-2,-7);
    }

    @Override
    public Transform getTransform(){
        return transform;
    }

    @Override
    public void update(float _deltaTime){

        count++;
        y += _deltaTime * speed;

        if(count % 10000 == 0){
            if(Math.random() < 0.5f){
                audioCrack.play(AudioLibrary.audioBufferIdMap.get("crack"));
            }else{
                audioCrack.play(AudioLibrary.audioBufferIdMap.get("doubleCrack"));
            }
        }

        if(count % 4000 == 0) {
            audioEngine.play(AudioLibrary.audioBufferIdMap.get("rotary"));
        }


        transform.setRotation(0,y,0);
    }

    public OpenGLMesh getMesh()
    {
        return mesh;
    }

    public void cleanup()
    {
        mesh.cleanup();
        audioEngine.cleanup();
        audioCrack.cleanup();
    }
}
