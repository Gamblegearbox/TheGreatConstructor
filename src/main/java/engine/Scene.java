package engine;

import gameObject.GameObject;
import math.Vector3;
import utils.Logger;
import utils.OBJLoader;

import java.util.ArrayList;
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
        wasLoaded = true;
        Material sceneMaterial = new Material(new Texture("/GameJam1807/Textures/Color.png"), null, new Texture("/GameJam1807/Textures/Gloss.png"), new Texture("/GameJam1807/Textures/Illum.png"));

        List<String> scnFileContent = utils.Utils.readAllLines(scnFilePath);

        List<String> objectDataFromFile = new ArrayList<>();


        //PARSE SCENE FILE
        for(String line : scnFileContent){

            if(line.startsWith("#")){
                continue;
            }

            if(line.startsWith("SCENE_TITLE")){
                String[] temp = line.split("=");
                line = temp[temp.length - 1];
                line = line.replaceAll("\"", "");
                line = line.trim();

                sceneName = line;
            }

            if(line.startsWith("LIGHT_POSITION")){
                String[] temp = line.split("=");
                line = temp[temp.length - 1];
                line = line.replaceAll("\"", "");
                line = line.trim();

                lightPosition = createPositionFromString(line);
            }

            if(line.startsWith("GO")){
                String[] temp = line.split("=");
                line = temp[temp.length - 1];

                objectDataFromFile.add(line);
            }

            if(line.startsWith("PO")){
                String[] temp = line.split("=");
                line = temp[temp.length - 1];

                objectDataFromFile.add(line);
            }
        }

        gameObjects = new GameObject[objectDataFromFile.size()];

        Logger.getInstance().writeln("> LOADING " + sceneName +  "...");

        for(int i = 0 ; i < gameObjects.length; i++)
        {
            String[] objectData = objectDataFromFile.get(i).split(";");
            String path = objectData[0].trim().replaceAll("\"", "");
            String position = objectData[1].trim();
            float boundingRadius = Float.parseFloat(objectData[2].trim());

            GameObject temp = new GameObject(OBJLoader.loadMesh(path), sceneMaterial, boundingRadius,false);
            temp.setPosition(createPositionFromString(position));
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
