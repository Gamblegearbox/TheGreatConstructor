package oldEngineStuff.engine.shading;

import oldEngineStuff.engine.texture.Texture;
import org.joml.Vector3f;

public class Material {

    private static final Vector3f DEFAULT_COLOR = new Vector3f(1.0f, 1.0f, 1.0f);
    private Vector3f color;
    private float reflectance;
    private Texture texture;
    private Texture normalMap;

    public Material()
    {
        color = DEFAULT_COLOR;
        reflectance = 0;
    }

    public Material(Vector3f color, float reflectance)
    {
        this();
        this.color = color;
        this.reflectance = reflectance;
    }

    public Material(Texture texture)
    {
        this();
        this.texture = texture;
    }

    public Material(Texture texture, float reflectance)
    {
        this();
        this.texture = texture;
        this.reflectance = reflectance;
    }

    public Vector3f getColour()
    {
        return color;
    }

    public void setColor(Vector3f color)
    {
        this.color = color;
    }

    public float getReflectance()
    {
        return reflectance;
    }

    public void setReflectance(float reflectance)
    {
        this.reflectance = reflectance;
    }

    public boolean isTextured()
    {
        return this.texture != null;
    }

    public Texture getTexture()
    {
        return texture;
    }

    public void setTexture(Texture texture)
    {
        this.texture = texture;
    }

    public boolean hasNormalMap()
    {
        return this.normalMap != null;
    }

    public Texture getNormalMap()
    {
        return normalMap;
    }

    public void setNormalMap(Texture normalMap)
    {
        this.normalMap = normalMap;
    }
}
