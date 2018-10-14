package utils;

import rendering.OpenGLMesh;

public class PrototypMeshes {

    public static OpenGLMesh cube()
    {
        float[] vertices = new float[]{
            0.5f, -0.5f, -0.5f,
            0.5f, -0.5f,  0.5f,
            -0.5f, -0.5f,  0.5f,
            -0.5f, -0.5f, -0.5f,

            0.5f,  0.5f, -0.5f,
            0.5f,  0.5f,  0.5f,
            -0.5f,  0.5f,  0.5f,
            -0.5f,  0.5f, -0.5f
        };

        float[] normals = new float[]{
            0.5f, -0.5f, -0.5f,
            0.5f, -0.5f,  0.5f,
            -0.5f, -0.5f,  0.5f,
            -0.5f, -0.5f, -0.5f,

            0.5f,  0.5f, -0.5f,
            0.5f,  0.5f,  0.5f,
            -0.5f,  0.5f,  0.5f,
            -0.5f,  0.5f, -0.5f
        };

        float[] uvCoords = new float[vertices.length / 3 * 2];

        int[] indices = new int[]{
            1,3,0,
            7,5,4,

            5,2,1,
            5,6,2,

            2,7,3,
            0,7,4,

            1,2,3,
            7,6,5,

            2,6,7,
            0,3,7,

            4,1,0,
            4,5,1,
        };

        return new OpenGLMesh(vertices, normals, uvCoords, indices, 1);
    }

    public static OpenGLMesh triangle()
    {
        float[] vertices = new float[]{
            0.00f,  0.5f, 0f,
            -0.5f, -0.5f, 0f,
            0.5f, -0.5f, 0f
        };

        float[] normals = new float[]{
            0,0,1,
            0,0,1,
            0,0,1
        };

        float[] uvCoords = new float[vertices.length / 3 * 2];

        int[] indices = new int[]{
            0, 1, 2,
        };

        return new OpenGLMesh(vertices, normals, uvCoords, indices, 1);
    }
}
