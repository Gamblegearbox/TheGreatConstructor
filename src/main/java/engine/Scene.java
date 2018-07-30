package engine;

import math.Vector3;
import utils.Logger;
import utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scene {

    private final String scnFilePath;

    private Vector3 lightPosition;
    private Map<String, GameObject> gameObjectMap;
    private String sceneName;
    private Material sceneMaterial;

    public Scene(String _path, Material sceneMaterial) throws Exception
    {
        scnFilePath = _path;
        this.sceneMaterial = sceneMaterial;

        gameObjectMap = new HashMap<>();
        load();
    }

    private void load() throws Exception
    {
        List<String> scnFileContent = utils.Utils.readAllLines(scnFilePath);

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

                lightPosition = Utils.createPositionFromString(line);
            }


        }

        Logger.getInstance().writeln("> INITIALIZING " + sceneName +  "...");
    }

    public  void addGameObject(String tag, GameObject go){
        gameObjectMap.put(tag, go);
    }

    public String getSceneName(){
        return sceneName;
    }

    public Vector3 getLightPosition()
    {
        return lightPosition;
    }

    public Material getSceneMaterial(){
        return sceneMaterial;
    }

    public Map<String, GameObject> getGameObjects()
    {
        return gameObjectMap;
    }

    public GameObject getGameObjectByTag(String tag){
        return gameObjectMap.get(tag);
    }

    public void update(float _deltaTime){
        for(GameObject temp: gameObjectMap.values()){
            temp.update(_deltaTime);
        }
    }

    public void cleanup()
    {
        Logger.getInstance().writeln("> CLEANING UP " + sceneName);
        for (GameObject temp : gameObjectMap.values())
        {
            temp.cleanup();
        }

    }
}
