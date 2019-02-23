package game;

import hud.FontTexture;
import rendering.Mesh;
import rendering.ShaderProgram;
import rendering.Texture;
import utils.OBJLoader;
import utils.Utils;

import java.awt.*;

public class Assets {

    //SHADER_CODE_FILES
    //VERTEX
    private static final String VERTEX_3D = "./res/shaders/VERTEX_3D.glsl";
    private static final String VERTEX_HUD = "./res/shaders/VERTEX_HUD.glsl";

    //GEOMETRY
    private static final String GEOMETRY_SMOOTH_FLAT_WIRE = "./res/shaders/GEOMETRY_SMOOTH_FLAT_WIRE.glsl";

    //FRAGMENT
    private static final String FRAGMENT_SCENE = "./res/shaders/FRAGMENT_SCENE.glsl";
    private static final String FRAGMENT_HUD = "./res/shaders/FRAGMENT_HUD.glsl";
    private static final String FRAGMENT_DEPTH = "./res/shaders/FRAGMENT_DEPTH.glsl";
    private static final String FRAGMENT_NORMALS = "./res/shaders/FRAGMENT_NORMALS.glsl";
    private static final String FRAGMENT_SOLID_WIREFRAME = "./res/shaders/FRAGMENT_SOLID_WIREFRAME.glsl";

    //SHADERS
    public static final ShaderProgram SHADER_SCENE = new ShaderProgram(
            Utils.loadResource(VERTEX_3D),
            Utils.loadResource(GEOMETRY_SMOOTH_FLAT_WIRE),
            Utils.loadResource(FRAGMENT_SCENE));

    public static final ShaderProgram SHADER_DEBUG_SOLID_WIREFRAME = new ShaderProgram(
            Utils.loadResource(VERTEX_3D),
            Utils.loadResource(GEOMETRY_SMOOTH_FLAT_WIRE),
            Utils.loadResource(FRAGMENT_SOLID_WIREFRAME));

    public static final ShaderProgram SHADER_DEBUG_NORMALS_TO_COLOR = new ShaderProgram(
            Utils.loadResource(VERTEX_3D),
            Utils.loadResource(GEOMETRY_SMOOTH_FLAT_WIRE),
            Utils.loadResource(FRAGMENT_NORMALS));

    public static final ShaderProgram SHADER_DEBUG_DEPTH_TO_COLOR = new ShaderProgram(
            Utils.loadResource(VERTEX_3D),
            Utils.loadResource(FRAGMENT_DEPTH));

    public static final ShaderProgram HUD_SHADER = new ShaderProgram(
            Utils.loadResource(VERTEX_HUD),
            Utils.loadResource(FRAGMENT_HUD));


    //MESHES
    public static final Mesh LOGO = OBJLoader.loadMesh(".\\res\\TestGameContent\\Meshes\\ui_logo.obj", 2);
    public static final Mesh NSX = OBJLoader.loadMesh(".\\res\\TestGameContent\\Meshes\\NS_X.obj", 2);
    public static final Mesh GTR = OBJLoader.loadMesh(".\\res\\TestGameContent\\Meshes\\GT_R.obj", 2);
    public static final Mesh ENV_1 = OBJLoader.loadMesh(".\\res\\TestGameContent\\Meshes\\environment.obj", 10);
    public static final Mesh WHEEL_01 = OBJLoader.loadMesh(".\\res\\TestGameContent\\Meshes\\Wheel_01.obj", 1);

    //TEXTURES
    public static final Texture ATLAS_COLORS = new Texture("/TestGameContent/Textures/MAP_COLOR.png");
    public static final Texture ATLAS_GLOSS = new Texture("/TestGameContent/Textures/MAP_GLOSS.png");
    public static final Texture ATLAS_EMIT = new Texture("/TestGameContent/Textures/MAP_EMIT.png");
    public static final Texture GRADIENT_LIGHT_COLORS = new Texture("/TestGameContent/Textures/gradient_lightColor.png");

    //FONT TEXTURES
    public static final Font FONT = new Font("Consolas", Font.PLAIN, 50);
    public static final String CHARSET = "ISO-8859-1";
    public static final FontTexture FONT_CONSOLAS = new FontTexture(FONT, CHARSET);
}
