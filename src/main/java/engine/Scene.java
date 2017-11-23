package engine;

import gameObject.GameObject;
import math.Vector3;


public class Scene {

    private final Vector3 lightPosition;
    private final GameObject[] gameObjects;


    public Scene(Vector3 _lightPosition, GameObject[] _gameObjects)
    {
        lightPosition = _lightPosition;
        gameObjects = _gameObjects;
    }

    public Vector3 getLightPosition()
    {
        return lightPosition;
    }

    public GameObject[] getGameObjects()
    {
        return gameObjects;
    }

    public void cleanup()
    {
        for(GameObject temp : gameObjects)
        {
            temp.cleanup();
        }
    }
}
