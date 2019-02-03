package game;

import rendering.OpenGLMesh;
import rendering.Texture;
import utils.OBJLoader;

public class Assets {

    //SHADERS

    //MESHES
    public static final OpenGLMesh LOGO = OBJLoader.loadMesh(".\\res\\TestGameContent\\Meshes\\ui_logo.obj", 2);
    public static final OpenGLMesh NSX = OBJLoader.loadMesh(".\\res\\TestGameContent\\Meshes\\NS_X.obj", 2);
    public static final OpenGLMesh GTR = OBJLoader.loadMesh(".\\res\\TestGameContent\\Meshes\\GT_R.obj", 2);
    public static final OpenGLMesh ENV_1 = OBJLoader.loadMesh(".\\res\\TestGameContent\\Meshes\\environment.obj", 10);
    public static final OpenGLMesh WHEEL_01 = OBJLoader.loadMesh(".\\res\\TestGameContent\\Meshes\\Wheel_01.obj", 1);

    //TEXTURES
    public static final Texture ATLAS_COLORS = new Texture("/TestGameContent/Textures/MAP_COLOR.png");
    public static final Texture ATLAS_GLOSS = new Texture("/TestGameContent/Textures/MAP_GLOSS.png");
    public static final Texture ATLAS_EMIT = new Texture("/TestGameContent/Textures/MAP_EMIT.png");
    public static final Texture GRADIENT_SHADING = new Texture("/TestGameContent/Textures/gradient_shading.png");
    public static final Texture GRADIENT_LIGHT_COLORS = new Texture("/TestGameContent/Textures/gradient_lightColor.png");
}
