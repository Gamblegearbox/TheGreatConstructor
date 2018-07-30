package game;

import engine.GameObject;
import interfaces.IF_GameObjectBehaviour;

public class PlayerBehaviour implements IF_GameObjectBehaviour {

    float time = 0;

    public void update(GameObject _go, float _deltaTime){
        time += _deltaTime;
        float sin = (float)Math.sin(time);
        _go.setPosition(_go.getPosition().x, sin*15f, _go.getPosition().z);
    }
}
