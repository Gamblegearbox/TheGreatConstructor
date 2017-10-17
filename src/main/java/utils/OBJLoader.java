package utils;


import engine.OpenGLMesh;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class OBJLoader {

    public static OpenGLMesh loadMesh(String fileName) throws Exception
    {
        List<String> lines = Utils.readAllLines(fileName);

        List<Vector3f> vertices = new ArrayList<>();
        List<Vector2f> textures = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Face> faces = new ArrayList<>();

        for (String line : lines)
        {
            String[] tokens = line.split("\\s+");

            switch (tokens[0])
            {
                case "v":
                    // Geometric vertex
                    Vector3f vec3f = new Vector3f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3]));
                    vertices.add(vec3f);
                    break;

                case "vt":
                    // Texture coordinate
                    Vector2f vec2f = new Vector2f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]));
                    textures.add(vec2f);
                    break;

                case "vn":
                    // Vertex normal
                    Vector3f vec3fNorm = new Vector3f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3]));
                    normals.add(vec3fNorm);
                    break;
                case "f":
                    Face face = new Face(tokens[1], tokens[2], tokens[3]);
                    faces.add(face);
                    break;
                default:
                    // Ignore other lines
                    break;
            }
        }
        return reorderLists(vertices, textures, normals, faces);
    }

    private static OpenGLMesh reorderLists(List<Vector3f> _verticesList, List<Vector2f> _uvCoordsList,
                                     List<Vector3f> _normalsList, List<Face> _facesList)
    {
        List<Integer> indicesList = new ArrayList<>();

        // Create vertices array in the order it has been declared
        float[] vertices = new float[_verticesList.size() * 3];
        int i = 0;
        for (Vector3f pos : _verticesList)
        {
            vertices[i * 3] = pos.x;
            vertices[i * 3 + 1] = pos.y;
            vertices[i * 3 + 2] = pos.z;
            i++;
        }
        float[] uvCoords = new float[_verticesList.size() * 2];
        float[] normals = new float[_verticesList.size() * 3];

        for (Face face : _facesList)
        {
            IdxGroup[] faceVertexIndices = face.getFaceVertexIndices();
            for (IdxGroup indValue : faceVertexIndices)
            {
                processFaceVertex(indValue, _uvCoordsList, _normalsList, indicesList, uvCoords, normals);
            }
        }

        int[] indices;          // = new int[indicesList.size()]; <-- redundant!
        indices = indicesList.stream().mapToInt((Integer v) -> v).toArray();

        return new OpenGLMesh(vertices, normals, uvCoords, indices);
    }

    private static void processFaceVertex(IdxGroup indices, List<Vector2f> textCoordList,
                                          List<Vector3f> normList, List<Integer> indicesList,
                                          float[] texCoordArr, float[] normArr) {

        // Set index for vertex coordinates
        int posIndex = indices.idxPos;
        indicesList.add(posIndex);

        // Reorder texture coordinates
        if (indices.idxTextCoord >= 0) {
            Vector2f textCoord = textCoordList.get(indices.idxTextCoord);
            texCoordArr[posIndex * 2] = textCoord.x;
            texCoordArr[posIndex * 2 + 1] = 1 - textCoord.y;
        }
        if (indices.idxVecNormal >= 0) {
            // Reorder vectornormals
            Vector3f vecNorm = normList.get(indices.idxVecNormal);
            normArr[posIndex * 3] = vecNorm.x;
            normArr[posIndex * 3 + 1] = vecNorm.y;
            normArr[posIndex * 3 + 2] = vecNorm.z;
        }
    }

    static class Face {

        /**
         * List of idxGroup groups for a face triangle (3 vertices per face).
         */
        private IdxGroup[] idxGroups = new IdxGroup[3];

        Face(String v1, String v2, String v3)
        {
            idxGroups = new IdxGroup[3];
            // Parse the lines
            idxGroups[0] = parseLine(v1);
            idxGroups[1] = parseLine(v2);
            idxGroups[2] = parseLine(v3);
        }

        private IdxGroup parseLine(String line)
        {
            IdxGroup idxGroup = new IdxGroup();

            String[] lineTokens = line.split("/");
            int length = lineTokens.length;
            idxGroup.idxPos = Integer.parseInt(lineTokens[0]) - 1;

            if (length > 1)
            {
                // It can be empty if the obj does not define text coords
                String textCoord = lineTokens[1];
                idxGroup.idxTextCoord = textCoord.length() > 0 ? Integer.parseInt(textCoord) - 1 : IdxGroup.NO_VALUE;
                if (length > 2)
                {
                    idxGroup.idxVecNormal = Integer.parseInt(lineTokens[2]) - 1;
                }
            }

            return idxGroup;
        }

        IdxGroup[] getFaceVertexIndices()
        {
            return idxGroups;
        }
    }

    static class IdxGroup {

        static final int NO_VALUE = -1;
        int idxPos;
        int idxTextCoord;
        int idxVecNormal;
        IdxGroup()
        {
            idxPos = NO_VALUE;
            idxTextCoord = NO_VALUE;
            idxVecNormal = NO_VALUE;
        }
    }
}
