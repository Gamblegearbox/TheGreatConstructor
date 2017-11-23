package engine;

public class Material {

    private final Texture diffuseMap;
    private final Texture normalMap;
    private final Texture glossMap;
    private final Texture illuminationMap;


    public Material(Texture _diffuseMap, Texture _normalMap, Texture _glossMap, Texture _illuminationMap)
    {
        diffuseMap = _diffuseMap;
        normalMap = _normalMap;
        glossMap = _glossMap;
        illuminationMap = _illuminationMap;
    }

    public Texture getDiffuseMap()
    {
        return diffuseMap;
    }

    public Texture getNormalMap()
    {
        return normalMap;
    }

    public Texture getGlossMap()
    {
        return glossMap;
    }

    public Texture getIlluminationMap()
    {
        return illuminationMap;
    }

    public boolean hasDiffuseMap()
    {
        return diffuseMap != null;
    }

    public boolean hasNormalMap()
    {
        return normalMap != null;
    }

    public boolean hasGlossMap()
    {
        return glossMap != null;
    }

    public boolean hasIlluminationMap()
    {
        return illuminationMap != null;
    }
}
