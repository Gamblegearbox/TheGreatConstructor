package game;

import engine.GameObject;
import interfaces.IF_GameObjectBehaviour;

public class LogoBehaviour implements IF_GameObjectBehaviour {

    float time = 0;
    float fastTime = 0;

    @Override
    public void update(GameObject _go, float _deltaTime) {


        time += _deltaTime;
        fastTime += _deltaTime * 2;
        float sin = (float)Math.sin(time);
        float fastSin = (float)Math.sin(fastTime);

        _go.setPosition(sin, 0, sin-15);
        _go.setRotation(fastSin*5, sin*20, sin*5);


    }
}
