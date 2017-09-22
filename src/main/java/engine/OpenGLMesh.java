package engine;

import org.lwjgl.BufferUtils;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;


public class OpenGLMesh {

    private int vaoID;
    private int vboID_vertices;
    private int vboID_colors;
    private int vboID_indices;
    private int vertexCount;


    public int getVaoID()
    {
        return vaoID;
    }

    public int getVertexCount()
    {
        return vertexCount;
    }



    public OpenGLMesh(float[] _vertices, float[] _normals, float[] _colors, float[] _uvCoords, int[] _indices)
    {
        vertexCount = _indices.length;

        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        vboID_vertices = glGenBuffers();
        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(_vertices.length);
        verticesBuffer.put(_vertices).flip();
        glBindBuffer(GL_ARRAY_BUFFER, vboID_vertices);
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        vboID_colors = glGenBuffers();
        FloatBuffer colorsBuffer = BufferUtils.createFloatBuffer(_colors.length);
        colorsBuffer.put(_colors).flip();
        glBindBuffer(GL_ARRAY_BUFFER, vboID_colors);
        glBufferData(GL_ARRAY_BUFFER, colorsBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);

        vboID_indices = glGenBuffers();
        IntBuffer indicesBuffer = BufferUtils.createIntBuffer(_indices.length);
        indicesBuffer.put(_indices).flip();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboID_indices);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public void cleanup()
    {
        glDisableVertexAttribArray(0);

        // Delete the VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vboID_vertices);
        glDeleteBuffers(vboID_colors);
        glDeleteBuffers(vboID_indices);

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoID);
    }

}
