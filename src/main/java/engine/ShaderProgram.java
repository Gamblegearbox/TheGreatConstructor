package engine;


import math.Matrix4;
import math.Vector3;
import org.lwjgl.system.MemoryStack;
import java.nio.FloatBuffer;
import static org.lwjgl.opengl.GL20.*;

class ShaderProgram {

    private final int programID;

    private int vertexShaderId;
    private int fragmentShaderId;

    ShaderProgram() throws Exception
    {
        programID = glCreateProgram();

        if (programID == 0)
        {
            throw new Exception("Could not create Shader");
        }
    }

    void createVertexShader(String shaderCode) throws Exception
    {
        vertexShaderId = createShader(shaderCode, GL_VERTEX_SHADER);
    }

    void createFragmentShader(String shaderCode) throws Exception
    {
        fragmentShaderId = createShader(shaderCode, GL_FRAGMENT_SHADER);
    }

    private int createShader(String shaderCode, int shaderType) throws Exception
    {
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

    void setUniformData(String _uniformName, Matrix4 _matrix)
    {
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
            fb.put(_matrix.getValues()).flip();
            glUniformMatrix4fv(uniformLocation, false, fb);
        }
    }

    void setUniformData(String _uniformName, int _value)
    {
        int uniformLocation = glGetUniformLocation(programID, _uniformName);
        glUniform1i(uniformLocation, _value);
    }

    void setUniformData(String _uniformName, Vector3 _value)
    {
        int uniformLocation = glGetUniformLocation(programID, _uniformName);
        glUniform3f(uniformLocation, _value.x, _value.y, _value.z);
    }

    void link() throws Exception
    {
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

    void bind()
    {
        glUseProgram(programID);
    }

    void unbind()
    {
        glUseProgram(0);
    }

    void cleanup()
    {
        unbind();
        if (programID != 0)
        {
            glDeleteProgram(programID);
        }
    }
}