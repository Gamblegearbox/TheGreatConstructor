package engine;

public class Material {

    private final Texture texture;
    private final Texture normalMap;

    public Material(Texture _texture)
    {
        texture = _texture;
        normalMap = null;
    }

    public Material(Texture _texture, Texture _normalMap)
    {
        texture = _texture;
        normalMap = _normalMap;
    }

    public Texture getTexture()
    {
        return texture;
    }

    public Texture getNormalMap()
    {
        return normalMap;
    }
}
