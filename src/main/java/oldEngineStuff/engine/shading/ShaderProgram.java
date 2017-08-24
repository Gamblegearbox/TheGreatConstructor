package oldEngineStuff.engine.shading;

import oldEngineStuff.engine.light.DirectionalLight;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {

    private final int programId;
    private int vertexShaderId;
    private int fragmentShaderId;
    private final Map<String, UniformData> uniforms;

    public ShaderProgram() throws Exception
    {
        programId = glCreateProgram();
        if (programId == 0)
        {
            throw new Exception("Could not create Shader");
        }
        uniforms = new HashMap<>();
    }

    public void createUniform(String uniformName) throws Exception
    {
        int uniformLocation = glGetUniformLocation(programId, uniformName);
        if (uniformLocation < 0)
        {
            throw new Exception("Could not find uniform:" + uniformName);
        }
        uniforms.put(uniformName, new UniformData(uniformLocation));
    }

    public void createDirectionalLightUniform(String uniformName) throws Exception
    {
        createUniform(uniformName + ".colour");
        createUniform(uniformName + ".direction");
        createUniform(uniformName + ".intensity");
    }

    public void createMaterialUniform(String uniformName) throws Exception
    {
        createUniform(uniformName + ".colour");
        createUniform(uniformName + ".hasTexture");
        createUniform(uniformName + ".reflectance");
    }

    public void setUniform(String uniformName, Matrix4f value)
    {
        UniformData uniformData = uniforms.get(uniformName);
        if (uniformData == null)
        {
            throw new RuntimeException("Uniform [" + uniformName + "] has nor been created");
        }
        // Check if float buffer has been created
        FloatBuffer fb = uniformData.getFloatBuffer();
        if (fb == null)
        {
            fb = BufferUtils.createFloatBuffer(16);
            uniformData.setFloatBuffer(fb);
        }
        // Dump the matrix into a float buffer
        value.get(fb);
        glUniformMatrix4fv(uniformData.getUniformLocation(), false, fb);
    }

    public void setUniform(String uniformName, int value)
    {
        UniformData uniformData = uniforms.get(uniformName);
        if (uniformData == null)
        {
            throw new RuntimeException("Uniform [" + uniformName + "] has nor been created");
        }
        glUniform1i(uniformData.getUniformLocation(), value);
    }

    public void setUniform(String uniformName, float value)
    {
        UniformData uniformData = uniforms.get(uniformName);
        if (uniformData == null)
        {
            throw new RuntimeException("Uniform [" + uniformName + "] has nor been created");
        }
        glUniform1f(uniformData.getUniformLocation(), value);
    }

    public void setUniform(String uniformName, Vector3f value)
    {
        UniformData uniformData = uniforms.get(uniformName);
        if (uniformData == null)
        {
            throw new RuntimeException("Uniform [" + uniformName + "] has nor been created");
        }
        glUniform3f(uniformData.getUniformLocation(), value.x, value.y, value.z);
    }

    public void setUniform(String uniformName, DirectionalLight dirLight)
    {
        setUniform(uniformName + ".colour", dirLight.getColor());
        setUniform(uniformName + ".direction", dirLight.getDirection());
        setUniform(uniformName + ".intensity", dirLight.getIntensity());
    }

    public void setUniform(String uniformName, Material material)
    {
        setUniform(uniformName + ".colour", material.getColour());
        setUniform(uniformName + ".hasTexture", material.isTextured() ? 1 : 0);
        setUniform(uniformName + ".reflectance", material.getReflectance());
    }

    public void createVertexShader(String shaderCode) throws Exception
    {
        vertexShaderId = createShader(shaderCode, GL_VERTEX_SHADER);
    }

    public void createFragmentShader(String shaderCode) throws Exception
    {
        fragmentShaderId = createShader(shaderCode, GL_FRAGMENT_SHADER);
    }

    protected int createShader(String shaderCode, int shaderType) throws Exception
    {
        int shaderId = glCreateShader(shaderType);
        if (shaderId == 0) {
            throw new Exception("Error creating shading. Code: " + shaderId);
        }

        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(shaderId, 1024));
        }

        glAttachShader(programId, shaderId);

        return shaderId;
    }

    public void link() throws Exception
    {
        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(programId, 1024));        }

        /*
        glValidateProgram(programId);
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(programId, 1024));
        }*/

    }

    public void bind()
    {
        glUseProgram(programId);
    }

    public void unbind()
    {
        glUseProgram(0);
    }

    public void cleanup()
    {
        unbind();
        if (programId != 0) {
            if (vertexShaderId != 0) {
                glDetachShader(programId, vertexShaderId);
            }
            if (fragmentShaderId != 0) {
                glDetachShader(programId, fragmentShaderId);
            }
            glDeleteProgram(programId);
        }
    }
}