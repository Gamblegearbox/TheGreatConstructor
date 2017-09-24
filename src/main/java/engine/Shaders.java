package engine;

import utils.Utils;

public class Shaders {

    public static ShaderProgram shaded;
    public static ShaderProgram wireframe;
    public static ShaderProgram shadedUnicolor;


    public static void initShaders() throws Exception
    {
        shaded = createShader("/shaders/shaded.vs", "/shaders/shaded.fs");
        wireframe = createShader("/shaders/wireframe.vs", "/shaders/wireframe.fs");
        shadedUnicolor = createShader("/shaders/unicolor.vs", "/shaders/unicolor.fs");
    }

    private static ShaderProgram createShader(String _vertexShaderPath, String _fragmentShaderPath) throws Exception
    {
        ShaderProgram shader = new ShaderProgram();
        shader.createVertexShader(Utils.loadResource(_vertexShaderPath));
        shader.createFragmentShader (Utils.loadResource(_fragmentShaderPath));
        shader.link();

        return shader;
    }
}
