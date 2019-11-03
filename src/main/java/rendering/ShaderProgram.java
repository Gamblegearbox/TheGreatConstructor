package rendering;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;
import java.nio.FloatBuffer;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;

public class ShaderProgram {

    private final int programID;

    private int vertexShaderId;
    private int geometryShaderId;
    private int fragmentShaderId;

    public ShaderProgram(String _vertexShaderCode, String _geometryShaderCode, String _fragmentShaderCode)
    {
        programID = glCreateProgram();

        if (programID == 0)
        {
            try {
                throw new Exception("Could not create Shader");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            createVertexShader(_vertexShaderCode);
            createGeometryShader(_geometryShaderCode);
            createFragmentShader(_fragmentShaderCode);
            link();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ShaderProgram(String _vertexShaderCode, String _fragmentShaderCode)
    {
        programID = glCreateProgram();

        if (programID == 0)
        {
            try {
                throw new Exception("Could not create Shader");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            createVertexShader(_vertexShaderCode);
            createFragmentShader(_fragmentShaderCode);
            link();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createVertexShader(String _shaderCode) throws Exception {
        vertexShaderId = createShader(_shaderCode, GL_VERTEX_SHADER);
    }

    private void createGeometryShader(String _shaderCode) throws Exception{
        geometryShaderId = createShader(_shaderCode, GL_GEOMETRY_SHADER);
    }

    private void createFragmentShader(String _shaderCode) throws Exception {
        fragmentShaderId = createShader(_shaderCode, GL_FRAGMENT_SHADER);
    }

    private int createShader(String shaderCode, int shaderType) throws Exception {
        int shaderId = glCreateShader(shaderType);

        if (shaderId == 0)
        {
            throw new Exception("Error creating shader. Type: " + shaderType);
        }

        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0)
        {
            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(shaderId, 1024));
        }

        glAttachShader(programID, shaderId);

        return shaderId;
    }

    public void setUniformData(String _uniformName, Matrix4f _matrix) {
        int uniformLocation = glGetUniformLocation(programID, _uniformName);

        /*
        PROVOKES GARBAGE COLLECTION VERY OFTEN!
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        buffer.put(_matrix.getValues()).flip();

        glUniformMatrix4fv(uniformLocation, false, buffer);
        */

        //THIS PREVENTS THE GARBAGE COLLECTOR TO HIT THE PROGRAM EVERY FEW SECONDS!
        try (MemoryStack stack = MemoryStack.stackPush()) {
            // Dump the matrix into a float buffer
            FloatBuffer fb = stack.mallocFloat(16);
            _matrix.get(fb);
            //fb.flip();
            glUniformMatrix4fv(uniformLocation, false, fb);
        }
    }

    public void setUniformData(String _uniformName, int _value) {

        int uniformLocation = glGetUniformLocation(programID, _uniformName);
        glUniform1i(uniformLocation, _value);
    }

    public void setUniformData(String _uniformName, float _value) {
        int uniformLocation = glGetUniformLocation(programID, _uniformName);
        glUniform1f(uniformLocation, _value);
    }

    public void setUniformData(String _uniformName, Vector3f _value) {
        int uniformLocation = glGetUniformLocation(programID, _uniformName);
        glUniform3f(uniformLocation, _value.x, _value.y, _value.z);
    }

    public void setUniformData(String _uniformName, float _x, float _y) {
        int uniformLocation = glGetUniformLocation(programID, _uniformName);
        glUniform2f(uniformLocation, _x, _y);
    }

    public void setUniformData(String _uniformName, float _x, float _y, float _z) {
        int uniformLocation = glGetUniformLocation(programID, _uniformName);
        glUniform3f(uniformLocation, _x, _y, _z);
    }

    public void setUniformData(String _uniformName, float _r, float _g, float _b, float _a) {
        int uniformLocation = glGetUniformLocation(programID, _uniformName);
        glUniform4f(uniformLocation, _r, _g, _b, _a);
    }

    private void link() throws Exception {
        glLinkProgram(programID);
        if (glGetProgrami(programID, GL_LINK_STATUS) == 0)
        {
            throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(programID, 1024));
        }

        if (vertexShaderId != 0)
        {
            glDetachShader(programID, vertexShaderId);
        }
        if (fragmentShaderId != 0)
        {
            glDetachShader(programID, fragmentShaderId);
        }

        glValidateProgram(programID);
        if (glGetProgrami(programID, GL_VALIDATE_STATUS) == 0)
        {
            System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(programID, 1024));
        }

    }

    public void bind()
    {
        glUseProgram(programID);
    }

    public void unbind()
    {
        glUseProgram(0);
    }

    public void cleanup() {
        unbind();
        if (programID != 0)
        {
            glDetachShader(programID, vertexShaderId);
            glDetachShader(programID, geometryShaderId);
            glDetachShader(programID, fragmentShaderId);
            glDeleteShader(vertexShaderId);
            glDeleteShader(geometryShaderId);
            glDeleteShader(fragmentShaderId);
            glDeleteProgram(programID);
        }
    }
}