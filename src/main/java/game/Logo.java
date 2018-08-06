package game;

import engine.MeshAndTransform;
import interfaces.IF_SceneObject;

public class Logo implements IF_SceneObject {

    float time = 0;
    float fastTime = 0;
    private MeshAndTransform meshAndTransform;

    public Logo(MeshAndTransform _meshAndTransform){
        meshAndTransform = _meshAndTransform;
    }

    @Override
    public MeshAndTransform getMeshAndTransform(){
        return meshAndTransform;
    }

    @Override
    public void update(float _deltaTime) {


        time += _deltaTime;
        fastTime += _deltaTime * 2;
        float sin = (float)Math.sin(time);
        float fastSin = (float)Math.sin(fastTime);

        meshAndTransform.setPosition(sin, 0, sin-15);
        meshAndTransform.setRotation(fastSin*5, sin*20, sin*5);


    }
}
