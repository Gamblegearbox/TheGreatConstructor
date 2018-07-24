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
    private List<OpenGLMesh> meshPool;
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
        List<OpenGLMesh> meshPool = new ArrayList<>();
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

            if(line.startsWith("SCENE_MATERIAL")){
                //TODO: load scene material here
            }

            if(line.startsWith("MESH")){
                objectDataFromFile.add(line);
            }
        }


        gameObjects = new GameObject[objectDataFromFile.size()];

        Logger.getInstance().writeln("> LOADING " + sceneName +  "...");

        for(int i = 0 ; i < gameObjects.length; i++) {

            String[] line = objectDataFromFile.get(i).split("=");
            String objectData = line[line.length - 1];

            String[] objectDataTokens = objectData.split(";");

            String path = objectDataTokens[0].trim().replaceAll("\"", "");
            float boundingRadius = Float.parseFloat(objectDataTokens[1].trim());

            GameObject temp = new GameObject(OBJLoader.loadMesh(path), sceneMaterial, boundingRadius,false);
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
