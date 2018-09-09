package engine;

import interfaces.IF_SceneObject;
import math.Vector3;
import utils.Logger;
import utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scene {

    private final String scnFilePath;

    private Vector3 lightPosition;
    private Map<String, IF_SceneObject> sceneObjects;
    private String sceneName;
    private Material sceneMaterial;

    public Scene(String _path, Material sceneMaterial) throws Exception
    {
        scnFilePath = _path;
        this.sceneMaterial = sceneMaterial;

        sceneObjects = new HashMap<>();
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

    public  void addSceneObject(String tag, IF_SceneObject _sceneObject){
        sceneObjects.put(tag, _sceneObject);
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

    public Map<String, IF_SceneObject> getGameObjects()
    {
        return sceneObjects;
    }

    public IF_SceneObject getSceneObjectByTag(String tag){
        return sceneObjects.get(tag);
    }

    public void update(float _deltaTime){
        for(IF_SceneObject temp: sceneObjects.values()){
            temp.update(_deltaTime);
        }
    }

    public void cleanup()
    {
        Logger.getInstance().writeln("> CLEANING UP " + sceneName);
        for (IF_SceneObject temp : sceneObjects.values())
        {
            temp.cleanup();
        }

    }
}
