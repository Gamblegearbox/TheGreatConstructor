package engine;

import utils.Utils;

public class Shaders {

    public static ShaderProgram sceneShader;
    public static ShaderProgram wireframeShader;

    public static void initShaders() throws Exception
    {
        //SCENE SHADER
        sceneShader = new ShaderProgram();
        sceneShader.createVertexShader(Utils.loadResource("/shaders/default.vs"));
        sceneShader.createFragmentShader (Utils.loadResource("/shaders/default.fs"));
        sceneShader.link();

        //WIREFRAME SHADER
        wireframeShader = new ShaderProgram();
        wireframeShader.createVertexShader(Utils.loadResource("/shaders/wireframe.vs"));
        wireframeShader.createFragmentShader (Utils.loadResource("/shaders/wireframe.fs"));
        wireframeShader.link();
    }
}
