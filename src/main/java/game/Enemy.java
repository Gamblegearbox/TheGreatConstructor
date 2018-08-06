package game;

import engine.MeshAndTransform;
import interfaces.IF_SceneObject;

public class Enemy implements IF_SceneObject {

    private float roty = 0;
    private float rotspeed = 10;
    MeshAndTransform meshAndTransform;

    public Enemy(MeshAndTransform _meshAndTransform){
        meshAndTransform = _meshAndTransform;
    }

    @Override
    public MeshAndTransform getMeshAndTransform(){
        return meshAndTransform;
    }

    @Override
    public void update(float _deltaTime){
        roty += _deltaTime * rotspeed;
        meshAndTransform.setRotation(0, roty,0);
    }
}
