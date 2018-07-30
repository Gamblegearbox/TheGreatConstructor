package engine;

import utils.Logger;
import utils.OBJLoader;
import utils.PrototypMeshes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MeshLibrary {

    public static final Map<String, OpenGLMesh> meshMap = new HashMap<>();

    public static void loadMeshes(String filePath)throws Exception {

        List<String> fileContent = utils.Utils.readAllLines(filePath);
        List<String> meshDataFromFile = new ArrayList<>();

        //PARSE MESH FILE
        for(String line : fileContent){

            if(line.startsWith("#")){
                continue;
            }

            if(line.startsWith("MESH")){
                meshDataFromFile.add(line);
            }
        }

        Logger.getInstance().writeln("> LOADING MESHES...");

        for(int i = 0 ; i < meshDataFromFile.size(); i++) {

            String[] line = meshDataFromFile.get(i).split("=");
            String objectData = line[line.length - 1];

            String[] objectDataTokens = objectData.split(";");
            String tag = objectDataTokens[0].trim().replaceAll("\"", "");
            String path = objectDataTokens[1].trim().replaceAll("\"", "");
            float boundingRadius = Float.parseFloat(objectDataTokens[2].trim());

            meshMap.put(tag, OBJLoader.loadMesh(path, boundingRadius));
        }
    }

    public static OpenGLMesh getMeshByTag(String tag){

        if(meshMap.containsKey(tag)){

            return meshMap.get(tag);
        }
        else{
            return PrototypMeshes.cube();
        }
    }
}
