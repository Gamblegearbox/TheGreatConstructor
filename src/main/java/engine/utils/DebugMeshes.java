package engine.utils;

import engine.mesh.Mesh;
import org.joml.Math;

public class DebugMeshes {

    public static Mesh buildHUDline()
    {
        float width = 0.1f;
        float height = 1;
        float depht = 0;

        float[] positions = new float[]{
                 width, height, depht,
                -width, height, depht,
                 width, 0,      depht,
                -width, 0,      depht
        };
        float[] texCoords = new float[]{0, 1, 0, 1, 0, 1, 0, 1};
        float[] normals = new float[]{
                0, 0, 1,
                0, 0, 1,
                0, 0, 1,
                0, 0, 1
        };
        int[] indices = new int[]{0, 2, 3, 3, 1, 0};

        return new Mesh(positions, texCoords, normals, indices);
    }

    public static Mesh buildHUDCircle(int sections)
    {
        float stepAngle = 360 / sections;
        float depth = 0f;

        float[] positions = new float[(sections + 1) * 3];
        float[] texCoords = new float[(sections + 1) * 2];
        float[] normals = new float[positions.length];
        int[] indices = new int[sections * 3];

        positions[positions.length-3] = 0;
        positions[positions.length-2] = 0;
        positions[positions.length-1] = depth;

        float currentAngle = 0;
        for(int i = 0; i < sections * 3; i+=3)
        {
            float x = (float)Math.cos(Math.toRadians(currentAngle));
            float y = (float)Math.sin(Math.toRadians(currentAngle));
            positions[i] = y;
            positions[i+1] = x;
            positions[i+2] = depth;

            normals[i] = 0;
            normals[i+1] = 0;
            normals[i+2] = 1;

            indices[i] = sections;
            indices[i+1] = i/3;
            indices[i+2] = i/3 + 1;

            currentAngle += stepAngle;
        }
        indices[indices.length-1] = 0; // the last index needs to be corrected to create a closed circle

        for(int i = 0; i < texCoords.length; i++)
        {
            texCoords[i] = 0;
        }

        return new Mesh(positions, texCoords, normals, indices);
    }

    public static Mesh buildHUDCompassNeedle()
    {
        float width = 0.5f;
        float height = 1;
        float depht = 0;

        float[] positions = new float[]{
                 0,      height, depht,
                -width,  -height,      depht,
                 0,      0, depht,
                 width,  -height,      depht
        };
        float[] texCoords = new float[]{0, 1, 0, 1, 0, 1, 0, 1};
        float[] normals = new float[]{
                0, 0, 1,
                0, 0, 1,
                0, 0, 1,
                0, 0, 1
        };
        int[] indices = new int[]{0, 1, 2, 0, 2, 3};

        return new Mesh(positions, texCoords, normals, indices);
    }

    public static Mesh buildQuad()
    {
        float halfSize = 0.5f;

        float[] positions = new float[]{
                -halfSize, 0.0f, -halfSize,
                -halfSize, 0.0f, halfSize,
                halfSize, 0.0f, -halfSize,
                halfSize, 0.0f, halfSize
        };
        float[] texCoords = new float[]{0, 1, 0, 1, 0, 1, 0, 1};
        float[] normals = new float[]{
                0, 1, 0,
                0, 1, 0,
                0, 1, 0,
                0, 1, 0
        };
        int[] indices = new int[]{0, 1, 2, 2, 1, 3};

        return new Mesh(positions, texCoords, normals, indices);
    }

    public static Mesh buildline()
    {
        float[] positions = new float[]{
                0.0f, 0.0f, 0.1f,
                0.0f, 0.0f, -0.1f,
                1.0f, 0.0f, 0.1f,
                1.0f, 0.0f, -0.1f
        };
        float[] texCoords = new float[]{0, 1, 0, 1, 0, 1, 0, 1};
        float[] normals = new float[]{
                0, 1, 0,
                0, 1, 0,
                0, 1, 0,
                0, 1, 0
        };
        int[] indices = new int[]{0, 2, 3, 3, 1, 0};

        return new Mesh(positions, texCoords, normals, indices);
    }
}
