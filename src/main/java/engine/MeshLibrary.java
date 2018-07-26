package engine;

import utils.Logger;
import utils.OBJLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MeshLibrary {

    public static final Map<String, OpenGLMesh> meshMap = new HashMap<>();;

    public static void loadMeshes(String filePath)throws Exception {

        System.out.println(filePath);
        List<String> scnFileContent = utils.Utils.readAllLines(filePath);
        List<String> objectDataFromFile = new ArrayList<>();

        //PARSE SCENE FILE
        for(String line : scnFileContent){

            if(line.startsWith("#")){
                continue;
            }

            if(line.startsWith("MESH")){
                objectDataFromFile.add(line);
            }
        }

        Logger.getInstance().writeln("> LOADING MESHES...");

        for(int i = 0 ; i < objectDataFromFile.size(); i++) {

            String[] line = objectDataFromFile.get(i).split("=");
            String objectData = line[line.length - 1];

            String[] objectDataTokens = objectData.split(";");
            String tag = objectDataTokens[0].trim().replaceAll("\"", "");
            String path = objectDataTokens[1].trim().replaceAll("\"", "");
            float boundingRadius = Float.parseFloat(objectDataTokens[2].trim());

            meshMap.put(tag, OBJLoader.loadMesh(path, boundingRadius));
        }
    }

    public static OpenGLMesh getMeshByTag(String tag){
        return meshMap.get(tag);
    }
}
