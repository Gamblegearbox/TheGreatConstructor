package game;

import rendering.OpenGLMesh;
import rendering.Texture;
import utils.OBJLoader;

public class Assets {

    //MESHES
    public static final OpenGLMesh LOGO = OBJLoader.loadMesh(".\\res\\TestGameContent\\Meshes\\ui_logo.obj", 2);
    public static final OpenGLMesh CAR_1 = OBJLoader.loadMesh(".\\res\\TestGameContent\\Meshes\\NSX.obj", 2);

    //TEXTURES
    public static final Texture ATLAS_COLORS = new Texture("/TestGameContent/Textures/MAP_COLOR.png");
    public static final Texture ATLAS_GLOSS = new Texture("/TestGameContent/Textures/MAP_GLOSS.png");
    public static final Texture ATLAS_EMIT = new Texture("/TestGameContent/Textures/MAP_EMIT.png");
    public static final Texture GRADIENT_SHADING = new Texture("/TestGameContent/Textures/gradient_shading.png");
    public static final Texture GRADIENT_LIGHT_COLORS = new Texture("/TestGameContent/Textures/gradient_lightColor.png");
}
