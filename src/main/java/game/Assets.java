package game;

import hud.FontTexture;
import rendering.Mesh;
import rendering.ShaderProgram;
import rendering.Texture;
import utils.OBJLoader;
import utils.Utils;

import java.awt.*;

public class Assets {

    //VERTEX SHADER_CODE_FILES
    private static final String VERTEX_3D = "./res/shaders/VERTEX_3D.glsl";
    private static final String VERTEX_WATER = "./res/shaders/VERTEX_WATER.glsl";
    private static final String VERTEX_HUD = "./res/shaders/VERTEX_HUD.glsl";

    //GEOMETRY SHADER_CODE_FILES
    private static final String GEOMETRY_SMOOTH_FLAT_WIRE = "./res/shaders/GEOMETRY_SMOOTH_FLAT_WIRE.glsl";

    //FRAGMENT SHADER_CODE_FILES
    private static final String FRAGMENT_SCENE = "./res/shaders/FRAGMENT_SCENE.glsl";
    private static final String FRAGMENT_HUD = "./res/shaders/FRAGMENT_HUD.glsl";
    private static final String FRAGMENT_DEPTH = "./res/shaders/FRAGMENT_DEPTH.glsl";
    private static final String FRAGMENT_NORMALS = "./res/shaders/FRAGMENT_NORMALS.glsl";
    private static final String FRAGMENT_SOLID_WIREFRAME = "./res/shaders/FRAGMENT_SOLID_WIREFRAME.glsl";

    //SHADERS
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

    public static final ShaderProgram SHADER_HUD = new ShaderProgram(
            Utils.loadResource(VERTEX_HUD),
            Utils.loadResource(FRAGMENT_HUD));

    private static final String TEST_VERTEX = "./res/shaders/MaterialTest/VERTEX.glsl";
    private static final String TEST_GEOMETRY = "./res/shaders/MaterialTest/GEOMETRY.glsl";
    private static final String TEST_FRAGMENT = "./res/shaders/MaterialTest/FRAGMENT.glsl";
    public static final ShaderProgram SHADER_DEBUG_TEST = new ShaderProgram(
            Utils.loadResource(TEST_VERTEX),
            Utils.loadResource(TEST_GEOMETRY),
            Utils.loadResource(TEST_FRAGMENT));

    //MESHES
    public static final Mesh BANNER = OBJLoader.loadMesh(".\\res\\TestGameContent\\Meshes\\banner.obj", 1);
    public static final Mesh BANNER_NIGHT_LIGHT = OBJLoader.loadMesh(".\\res\\TestGameContent\\Meshes\\bannerNightLight.obj", 1);
    public static final Mesh CUBE = OBJLoader.loadMesh(".\\res\\TestGameContent\\Meshes\\cube.obj", 1);
    public static final Mesh NS_X = OBJLoader.loadMesh(".\\res\\TestGameContent\\Meshes\\NS_X.obj", 2);
    public static final Mesh GT_R = OBJLoader.loadMesh(".\\res\\TestGameContent\\Meshes\\GT_R.obj", 2);
    public static final Mesh CT_R = OBJLoader.loadMesh(".\\res\\TestGameContent\\Meshes\\CT_R.obj", 2);
    public static final Mesh RX_S = OBJLoader.loadMesh(".\\res\\TestGameContent\\Meshes\\RX_S.obj", 2);

    //TEXTURES
    public static final Texture ATLAS_COLORS = new Texture("/TestGameContent/Textures/Atlas_CarsDiffuse.png");
    public static final Texture ATLAS_NORMALS = new Texture("/TestGameContent/Textures/MaterialTest/normalMap.png");
    public static final Texture ATLAS_MATERIAL_INFO = new Texture("/TestGameContent/Textures/Atlas_CarsMaterialInfo.png");

    public static final Texture GRADIENT_LIGHT_COLORS = new Texture("/TestGameContent/Textures/gradient_lightColor.png");
    public static final Texture REFLECTION_MAP_DAY = new Texture("/TestGameContent/Textures/reflectionMap.png");
    public static final Texture REFLECTION_MAP_NIGHT = new Texture("/TestGameContent/Textures/MaterialTest/reflectionMap_night.png");
    public static final Texture SHADOW_PATTERN = new Texture("/TestGameContent/Textures/shadowPattern.png");

    public static final float TEXTURE_ATLAS_COLOR_U_OFFSET = 1.0f / 16.0f;
    public static final float TEXTURE_ATLAS_BORDER_U_OFFSET = TEXTURE_ATLAS_COLOR_U_OFFSET * 0.5f;
    public static final float TEXTURE_ATLAS_COLOR_V_OFFSET = 1.0f / 4.0f;
    public static final float TEXTURE_ATLAS_BORDER_V_OFFSET = TEXTURE_ATLAS_COLOR_V_OFFSET * 0.5f;

    public static final float[] uvCoords = new float[2];
    public static float[] getUvForColorFromAtlas(int _x, int _y){

        uvCoords[0] = Assets.TEXTURE_ATLAS_BORDER_U_OFFSET + Assets.TEXTURE_ATLAS_COLOR_U_OFFSET * _x;
        uvCoords[1] = Assets.TEXTURE_ATLAS_BORDER_V_OFFSET + Assets.TEXTURE_ATLAS_COLOR_V_OFFSET * _y;

        return uvCoords;
    }

    //FONT TEXTURES
    public static final Font FONT = new Font("Consolas", Font.PLAIN, 20);
    public static final String CHARSET = "ISO-8859-1";
    public static final FontTexture FONT_CONSOLAS = new FontTexture(FONT, CHARSET);

}
