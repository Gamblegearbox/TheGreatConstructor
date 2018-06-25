package engine;

import gameObject.GameObject;
import math.Vector3;
import utils.Logger;
import utils.OBJLoader;

import java.util.List;


public class Scene {

    private final String scnFilePath;

    private Vector3 lightPosition;
    private GameObject[] gameObjects;
    private String sceneName;
    private boolean wasLoaded;

    public Scene(String _path)
    {
        wasLoaded = false;
        scnFilePath = _path;
    }

    public void load() throws Exception
    {
        Material sceneMaterial = new Material(new Texture("/textures/iav.png"), new Texture("/textures/car_normals.png"), new Texture("/textures/cube/gloss.png"), new Texture("/textures/cube/illumination.png"));

        wasLoaded = true;
        List<String> scnFileContent = utils.Utils.readAllLines(scnFilePath);

        sceneName = scnFileContent.get(0);
        lightPosition = createPositionFromString(scnFileContent.get(1));

        Logger.getInstance().writeln("> LOADING " + sceneName +  "...");

        //TODO: refine this for multiple objects
        gameObjects = new GameObject[1];
        String[] pathAndPos = scnFileContent.get(3).split(";");

        for(int i = 0 ; i < gameObjects.length; i++)
        {
            GameObject temp = new GameObject(OBJLoader.loadMesh(pathAndPos[0]), sceneMaterial, 2f);
            temp.setPosition(createPositionFromString(pathAndPos[1]));
            gameObjects[i] = temp;
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

    private Vector3 createPositionFromString(String _string)
    {
        String[] positionAsString = _string.split(",");
        float x = Float.parseFloat(positionAsString[0]);
        float y = Float.parseFloat(positionAsString[1]);
        float z = Float.parseFloat(positionAsString[2]);

        return new Vector3(x, y, z);
    }
}
