package game;

import engine.MeshAndTransform;
import interfaces.IF_SceneObject;

public class Player implements IF_SceneObject {

    float time = 0;
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
        time += _deltaTime;
        float sin = (float)Math.sin(time);
        meshAndTransform.setPosition(meshAndTransform.getPosition().x, sin*15f, meshAndTransform.getPosition().z);
    }
}
