package game;

import engine.GameObject;
import interfaces.IF_GameObjectBehaviour;

public class EnemyBehaviour implements IF_GameObjectBehaviour {

    private float roty = 0;
    private float rotspeed = 10;

    public void update(GameObject _go, float _deltaTime){
        roty += _deltaTime * rotspeed;
        _go.setRotation(0, roty,0);
    }
}
