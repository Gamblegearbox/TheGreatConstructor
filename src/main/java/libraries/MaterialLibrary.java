package libraries;

import rendering.Material;
import rendering.Texture;
import utils.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MaterialLibrary {

    public static final Map<String, Material> materialMap = new HashMap<>();

    public static void loadMaterials(String filePath)throws Exception {

        List<String> fileContent = utils.Utils.readAllLines(filePath);
        List<String> materialDataFromFile = new ArrayList<>();

        //PARSE MESH FILE
        for(String line : fileContent){

            if(line.startsWith("#")){
                continue;
            }

            if(line.startsWith("MATERIAL")){
                materialDataFromFile.add(line);
            }
        }

        Logger.getInstance().writeln(">>> LOADING MATERIALS...");

        for(int i = 0 ; i < materialDataFromFile.size(); i++) {

            String[] line = materialDataFromFile.get(i).split("=");
            String objectData = line[line.length - 1];

            String[] objectDataTokens = objectData.split(";");
            String tag = objectDataTokens[0].trim().replaceAll("\"", "");
            String diffusePath = objectDataTokens[1].trim().replaceAll("\"", "");
            String normalPath = objectDataTokens[2].trim().replaceAll("\"", "");
            String glossPath = objectDataTokens[3].trim().replaceAll("\"", "");
            String illumPath = objectDataTokens[4].trim().replaceAll("\"", "");

            Texture diffuse = diffusePath.equals("none") ? null : new Texture(diffusePath);
            Texture normal = normalPath.equals("none") ? null : new Texture(normalPath);
            Texture gloss = glossPath.equals("none") ? null : new Texture(glossPath);
            Texture illum = illumPath.equals("none") ? null : new Texture(illumPath);

            materialMap.put(tag, new Material(diffuse, normal, gloss, illum));
        }
    }

    public static Material getMaterialByTag(String tag){

        if(materialMap.containsKey(tag)){

            return materialMap.get(tag);
        }
        else{
            return new Material(null, null, null, null);
        }
    }
}
