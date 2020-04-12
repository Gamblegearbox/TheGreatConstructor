package hud;

import interfaces.IF_HudItem;
import rendering.ShaderProgram;
import rendering.Transform;
import rendering.Mesh;


import java.util.ArrayList;
import java.util.List;

public class TextItem implements IF_HudItem {

    private static final float ZPOS = 0.0f;
    private static final float BOUNDING_RADIUS = 1.0f;
    private static final int VERTICES_PER_QUAD = 4;

    private final FontTexture fontTexture;
    private final Transform transform;
    private final ShaderProgram shader;

    private Mesh mesh;
    private String text;

    public TextItem(String _text, FontTexture _fontTexture, ShaderProgram _shader){
        text = _text;
        fontTexture = _fontTexture;

        transform = new Transform();
        shader = _shader;
        mesh = buildMesh();
    }

    @Override
    public Transform getTransform() {
        return transform;
    }

    @Override
    public Mesh getMesh() {
        return mesh;
    }

    @Override
    public ShaderProgram getShader(){
        return shader;
    }

    @Override
    public void update(float _deltaTime) {

    }

    public FontTexture getFontTexture(){
        return fontTexture;
    }

    public String getText(){
        return text;
    }

    public void setText(String _text){
        this.text = _text;
        this.mesh.cleanup();
        this.mesh = buildMesh();
    }

    private Mesh buildMesh(){

        List<Float> positionsList = new ArrayList();
        List<Float> uvCoordsList = new ArrayList();
        float[] normals = new float[0];
        List<Integer> indicesList = new ArrayList();

        char[] chars = text.toCharArray();

        float startX = 0f;
        for(int i = 0; i < chars.length; i++){

            FontTexture.CharInfo charInfo = fontTexture.getCharInfo(chars[i]);
            // VERTEX TOP LEFT
            positionsList.add(startX);                           //x
            positionsList.add(0.0f);                             //y
            positionsList.add(ZPOS);                             //z
            uvCoordsList.add((float)charInfo.getStartX() / (float) fontTexture.getWidth());
            uvCoordsList.add(0.0f);
            indicesList.add(i*VERTICES_PER_QUAD);

            // VERTEX BOTTOM LEFT
            positionsList.add(startX);                           //x
            positionsList.add((float)fontTexture.getHeight());   //y
            positionsList.add(ZPOS);                             //z
            uvCoordsList.add((float)charInfo.getStartX() / (float) fontTexture.getWidth());
            uvCoordsList.add(1.0f);
            indicesList.add(i*VERTICES_PER_QUAD + 1);

            // VERTEX BOTTOM RIGHT
            positionsList.add(startX + charInfo.getWidth());     //x
            positionsList.add((float)fontTexture.getHeight());   //y
            positionsList.add(ZPOS);                             //z
            uvCoordsList.add((float)(charInfo.getStartX() + charInfo.getWidth() )/ (float)fontTexture.getWidth());
            uvCoordsList.add(1.0f);
            indicesList.add(i*VERTICES_PER_QUAD + 2);

            // VERTEX TOP RIGHT
            positionsList.add(startX + charInfo.getWidth());     //x
            positionsList.add(0.0f);                             //y
            positionsList.add(ZPOS);                             //z
            uvCoordsList.add((float)(charInfo.getStartX() + charInfo.getWidth() ) / (float)fontTexture.getWidth());
            uvCoordsList.add(0.0f);
            indicesList.add(i*VERTICES_PER_QUAD + 3);

            // INDICES TO CLOSE 2ND TRIANGLE
            indicesList.add(i*VERTICES_PER_QUAD);
            indicesList.add(i*VERTICES_PER_QUAD + 2);

            startX += charInfo.getWidth();
        }

        // CONVERT TO ARRAYS
        float[] positions = new float[positionsList.size()];
        for(int i = 0; i < positionsList.size(); i++){
            positions[i] = positionsList.get(i);
        }

        float[] uvCoords = new float[uvCoordsList.size()];
        for(int i = 0; i < uvCoordsList.size(); i++){
            uvCoords[i] = uvCoordsList.get(i);
        }

        int[] indices = new int[indicesList.size()];
        for(int i = 0; i < indicesList.size(); i++){
            indices[i] = indicesList.get(i);
        }

        //TODO: replace with update mesh data! put "new" into contructor!
        return new Mesh(positions, normals, uvCoords, indices, BOUNDING_RADIUS);
    }

    @Override
    public void cleanup() {
        mesh.cleanup();
    }

}
