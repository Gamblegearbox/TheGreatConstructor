package game;

import engine.MeshAndTransform;
import interfaces.IF_SceneObject;

public class Player implements IF_SceneObject {

    float time = 0;
    float rotSpeed = 5.0f;

    MeshAndTransform meshAndTransform;

    public Player(MeshAndTransform _meshAndTransform){
        meshAndTransform = _meshAndTransform;
    }

    @Override
    public MeshAndTransform getMeshAndTransform(){
        return meshAndTransform;
    }

    @Override
    public void update(float _deltaTime){
        time += _deltaTime * rotSpeed;

        meshAndTransform.setRotation(0, time,0);
    }
}
