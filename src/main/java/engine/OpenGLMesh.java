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

    public static final int VERTICES = 0;
    public static final int NORMALS = 1;
    public static final int UV_COORDS = 2;

    private final float boundingRadius;
    private final int vaoID;
    private final int vboID_vertices;
    private final int vboID_normals;
    private final int vboID_uvCoords;
    private final int vboID_indices;
    private final int vertexCount;
    private final int indicesCount;


    private FloatBuffer verticesBuffer;

    public OpenGLMesh(float[] _vertices, float[] _normals, float[] _uvCoords, int[] _indices, float _boundingRadius)
    {
        boundingRadius = _boundingRadius;
        vertexCount = _vertices.length / 3;
        indicesCount = _indices.length;

        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        vboID_vertices = glGenBuffers();
        verticesBuffer = BufferUtils.createFloatBuffer(_vertices.length);
        updateVertices(_vertices);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        vboID_normals = glGenBuffers();
        FloatBuffer normalsBuffer = BufferUtils.createFloatBuffer(_normals.length);
        normalsBuffer.put(_normals).flip();
        glBindBuffer(GL_ARRAY_BUFFER, vboID_normals);
        glBufferData(GL_ARRAY_BUFFER, normalsBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);

        vboID_uvCoords = glGenBuffers();
        FloatBuffer uvBuffer = BufferUtils.createFloatBuffer(_uvCoords.length);
        uvBuffer.put(_uvCoords).flip();
        glBindBuffer(GL_ARRAY_BUFFER, vboID_uvCoords);
        glBufferData(GL_ARRAY_BUFFER, uvBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(2, 2, GL_FLOAT, false, 0, 0);

        vboID_indices = glGenBuffers();
        IntBuffer indicesBuffer = BufferUtils.createIntBuffer(_indices.length);
        indicesBuffer.put(_indices).flip();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboID_indices);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public void updateVertices(float[] _vertices){
        verticesBuffer.put(_vertices).flip();
        glBindBuffer(GL_ARRAY_BUFFER, vboID_vertices);
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
    }

    public int getVaoID()
    {
        return vaoID;
    }

    public int getVertexCount()
    {
        return vertexCount;
    }

    public int getIndicesCount(){
        return indicesCount;
    }

    public float getBoundingRadius()
    {
        return boundingRadius;
    }

    public void cleanup()
    {
        glDisableVertexAttribArray(0);

        // Delete the VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vboID_vertices);
        glDeleteBuffers(vboID_normals);
        glDeleteBuffers(vboID_uvCoords);
        glDeleteBuffers(vboID_indices);

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoID);
    }

}
