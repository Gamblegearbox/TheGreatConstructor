package game;

import engine.MeshAndTransform;
import interfaces.IF_SceneObject;

public class Player implements IF_SceneObject {

    float x = 0;
    float z = 0;
    float y = 0;
    float speed = 5.0f;

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


        x = 3f;
        y += _deltaTime * speed;
        z = -10f;

        if(y > 8){
            y = -8;
        }


        meshAndTransform.setPosition(x,y,z);
    }
}
