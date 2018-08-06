package game;

import engine.MeshAndTransform;
import interfaces.IF_SceneObject;
import math.Vector3;

public class Projectile implements IF_SceneObject{

    private MeshAndTransform meshAndTransform;

    private float baseSpeed = 100f;

    private float xRange = 25f;
    private float speedModRange = 25f;
    private float currentSpeed = baseSpeed;

    private boolean isActive;

    public Projectile(MeshAndTransform _meshAndTransform){
        meshAndTransform = _meshAndTransform;
        meshAndTransform.setRotation(0,-90,0);
        isActive = false;
        meshAndTransform.setVisibility(false);
    }

    @Override
    public MeshAndTransform getMeshAndTransform(){
        return meshAndTransform;
    }

    @Override
    public void update(float _deltaTime){

        if(isActive) {
            meshAndTransform.getPosition().x += currentSpeed * _deltaTime;

            if(meshAndTransform.getPosition().x > xRange) {
                stop();
            }
        }


    }

    public void start(Vector3 _pos){
        currentSpeed = (((float)Math.random() -0.5f) * 2 * speedModRange) + baseSpeed;
        meshAndTransform.setPosition(_pos);
        meshAndTransform.setVisibility(true);
        isActive = true;
    }

    public void stop(){
        meshAndTransform.setVisibility(false);
        isActive = false;
    }

}
