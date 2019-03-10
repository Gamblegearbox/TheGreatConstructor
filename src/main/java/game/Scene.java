package game;

import interfaces.IF_SceneItem;
import utils.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scene {

    private final String scnFilePath;

    private final Map<String, IF_SceneItem> sceneObjects;
    private String sceneName;

    public Scene(String _path) throws Exception
    {
        scnFilePath = _path;

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
        }

        Logger.getInstance().writeln("> INITIALIZING " + sceneName +  "...");
    }

    public  void addSceneObject(String tag, IF_SceneItem _sceneObject){
        sceneObjects.put(tag, _sceneObject);
    }

    public String getSceneName(){
        return sceneName;
    }

    public Map<String, IF_SceneItem> getSceneItems()
    {
        return sceneObjects;
    }

    public IF_SceneItem getSceneObjectByTag(String tag){
        return sceneObjects.get(tag);
    }

    public void update(float _deltaTime){
        for(IF_SceneItem temp: sceneObjects.values()){
            temp.update(_deltaTime);
        }
    }

    public void cleanup()
    {
        Logger.getInstance().writeln(">>> CLEANING UP " + sceneName);
        for (IF_SceneItem temp : sceneObjects.values())
        {
            temp.cleanup();
        }

    }
}
