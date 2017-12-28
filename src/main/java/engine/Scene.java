package engine;

import gameObject.GameObject;
import math.Vector3;
import utils.Logger;
import utils.OBJLoader;


public class Scene {

    private final Vector3 lightPosition;
    private final GameObject[] gameObjects;
    private final String sceneName;
    private boolean wasLoaded;

    //todo: temporary
    private String loadpath;    //todo: textfile with load paths?
    private Material sceneMaterial;

    public Scene(String _sceneName, Vector3 _lightPosition, int _numberOfObjects, String _path, Material _material)
    {
        sceneName = _sceneName;
        lightPosition = _lightPosition;
        gameObjects = new GameObject[_numberOfObjects];
        wasLoaded = false;
        loadpath = _path;
        sceneMaterial = _material;
    }

    public void load() throws Exception
    {
        wasLoaded = true;

        Logger.getInstance().writeln("> LOADING " + sceneName +  "...");

        //todo: load objects here (experimental)
        float x = 1;
        float y = -1.5f;
        float z = -3f;
        Vector3 position = new Vector3(x,y,z);

        for(int i = 0 ; i < gameObjects.length; i++)
        {
            position.set(x, y, z);

            GameObject temp = new GameObject(OBJLoader.loadMesh(loadpath), sceneMaterial, 2f);
            temp.setPosition(position);
            gameObjects[i] = temp;
            z-=3f;
        }

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
        if(wasLoaded)
        {
            Logger.getInstance().writeln("> CLEANING UP " + sceneName);
            for (GameObject temp : gameObjects)
            {
                temp.cleanup();
            }
        }
    }
}
