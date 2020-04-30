package rendering;

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


public class Mesh {

    public static final int VERTICES = 0;
    public static final int NORMALS = 1;
    public static final int UV_COORDS = 2;

    private final int vaoID;
    private final int vboID_vertices;
    private final int vboID_normals;
    private final int vboID_uvCoords;
    private final int vboID_indices;
    private final int vertexCount;
    private final int indicesCount;
    private boolean isVisible;
    private float boundingRadius;

    private FloatBuffer verticesBuffer;
    private FloatBuffer uvBuffer;
    private FloatBuffer normalsBuffer;
    private IntBuffer indicesBuffer;

    public Mesh(float[] _vertices, float[] _normals, float[] _uvCoords, int[] _indices, float _boundingRadius) {
        boundingRadius = _boundingRadius;
        vertexCount = _vertices.length / 3;
        indicesCount = _indices.length;

        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        vboID_vertices = glGenBuffers();
        updateVertices(_vertices);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        vboID_normals = glGenBuffers();
        updateNormals(_normals);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);

        vboID_uvCoords = glGenBuffers();
        updateUVCoords(_uvCoords);
        glVertexAttribPointer(2, 2, GL_FLOAT, false, 0, 0);

        vboID_indices = glGenBuffers();
        updateIndices(_indices);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

        setVisibility(true);
    }

    public void setVisibility(boolean _value)
    {
        isVisible = _value;
    }

    public boolean isVisible()
    {
        return isVisible;
    }

    private void updateVertices(float[] _vertices){
        verticesBuffer = BufferUtils.createFloatBuffer(_vertices.length);
        verticesBuffer.put(_vertices).flip();
        glBindBuffer(GL_ARRAY_BUFFER, vboID_vertices);
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
    }

    private void updateUVCoords(float[] _uvCoords){
        uvBuffer = BufferUtils.createFloatBuffer(_uvCoords.length);
        uvBuffer.put(_uvCoords).flip();
        glBindBuffer(GL_ARRAY_BUFFER, vboID_uvCoords);
        glBufferData(GL_ARRAY_BUFFER, uvBuffer, GL_STATIC_DRAW);
    }

    private void updateNormals(float[] _normals){
        normalsBuffer = BufferUtils.createFloatBuffer(_normals.length);
        normalsBuffer.put(_normals).flip();
        glBindBuffer(GL_ARRAY_BUFFER, vboID_normals);
        glBufferData(GL_ARRAY_BUFFER, normalsBuffer, GL_STATIC_DRAW);
    }

    private void updateIndices(int[] _indices){
        indicesBuffer = BufferUtils.createIntBuffer(_indices.length);
        indicesBuffer.put(_indices).flip();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboID_indices);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
    }

    private void updateBoundingRadius(float _radius){
        boundingRadius = _radius;
    }

    public void updateMeshData(float[] _vertices, float[] _normals, float[] _uvCoords, int[] _indices, float _boundingRadius){
        glBindVertexArray(vaoID);

        updateVertices(_vertices);
        updateNormals(_normals);
        updateUVCoords(_uvCoords);
        updateIndices(_indices);
        updateBoundingRadius(_boundingRadius);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
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
