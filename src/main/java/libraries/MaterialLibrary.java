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
    public static final Map<String, Texture> gradientMap = new HashMap<>();

    public static void loadMaterials(String filePath)throws Exception {

        List<String> fileContent = utils.Utils.readAllLines(filePath);
        List<String> materialDataFromFile = new ArrayList<>();
        List<String> gradientDataFromFile = new ArrayList<>();

        //PARSE MESH FILE
        for(String line : fileContent){

            if(line.startsWith("MATERIAL")){
                materialDataFromFile.add(line);
            }

            if(line.startsWith("GRADIENT")){
                gradientDataFromFile.add(line);
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

            Logger.getInstance().writeln("\t\tTAG: " + tag + "\n\t\t\tDIFFUSE: " + diffusePath + "\n\t\t\tNORMALS: " + normalPath + "\n\t\t\tGLOSS: " + glossPath + "\n\t\t\tILLUMN: " + illumPath);

            materialMap.put(tag, new Material(diffuse, normal, gloss, illum));
        }

        Logger.getInstance().writeln(">>> LOADING GRADIENTS...");

        for(int i = 0 ; i < gradientDataFromFile.size(); i++) {

            String[] line = gradientDataFromFile.get(i).split("=");
            String objectData = line[line.length - 1];

            String[] objectDataTokens = objectData.split(";");
            String tag = objectDataTokens[0].trim().replaceAll("\"", "");
            String path = objectDataTokens[1].trim().replaceAll("\"", "");

            Logger.getInstance().writeln("\t\t" + path);

            gradientMap.put(tag, new Texture(path));
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

    public static Texture getGradientByTag(String tag){
        return gradientMap.get(tag);
        //TODO: fallback?
    }
}
