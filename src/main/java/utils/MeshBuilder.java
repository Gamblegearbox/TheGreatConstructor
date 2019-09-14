package utils;

import game.Assets;
import game.Game;
import rendering.Mesh;

public class MeshBuilder {

    public static Mesh createCube()
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

        return new Mesh(vertices, normals, uvCoords, indices, 1);
    }

    public static Mesh createTriangle()
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

        return new Mesh(vertices, normals, uvCoords, indices, 1);
    }

    public static Mesh createPlane(float _width, float _length, int _resWidth, int _resLength, int _colorCoordX, int _colorCoordY){

        float[] verts = new float[(_resLength + 1) * (_resWidth + 1) * 3];
        float[] normals = new float[verts.length];
        float[] uvCoords = new float[(_resLength + 1) * (_resWidth + 1) * 2];
        int[] indices = new int[_resLength * _resWidth * 6];

        int index = 0;
        for(int z = 0; z < _resLength + 1; z++){
            for(int x = 0; x < _resWidth + 1; x++){
                verts[index + 0] = (x * (_width / _resWidth)) - (_width / 2);
                verts[index + 1] = 0f;
                verts[index + 2] = z * (_length / _resLength) - (_length / 2);
                index += 3;
            }
        }

        for(int i = 0; i < normals.length; i+=3){
            normals[i+0] = 0f;
            normals[i+1] = 1f;
            normals[i+2] = 0f;
        }

        for(int i = 0; i < uvCoords.length; i+=2){
            float[] atlasCoords = Assets.getUvForColorFromAtlas(_colorCoordX, _colorCoordY);
            uvCoords[i] = atlasCoords[0];
            uvCoords[i+1] = atlasCoords[1];
        }

        for (int ti = 0, vi = 0, y = 0; y < _resLength; y++, vi++) {
            for (int x = 0; x < _resWidth; x++, ti += 6, vi++) {
                indices[ti + 0] = vi + 0;
                indices[ti + 1] = indices[ti + 4] = vi + _resWidth + 1;
                indices[ti + 2] = indices[ti + 3] = vi + 1;
                indices[ti + 5] = vi + _resWidth + 2;
            }
        }

        return new Mesh(verts, normals, uvCoords, indices, Math.max(_width, _length));
    }
}
