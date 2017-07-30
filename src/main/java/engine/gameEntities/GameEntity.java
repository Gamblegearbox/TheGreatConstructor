package engine.gameEntities;

import engine.mesh.Mesh;
import engine.utils.Conversions;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class GameEntity {

    private Mesh mesh;
    private final Vector3f position;
    private final Quaternionf rotation;
    private Vector3f scale;

    public GameEntity()
    {
        position = new Vector3f(0, 0, 0);
        scale = new Vector3f(1, 1, 1);
        rotation = new Quaternionf();
    }

    public GameEntity(Mesh mesh)
    {
        this();
        this.mesh = mesh;
    }

    public Vector3f getPosition()
    {
        return position;
    }

    public void setPosition(float x, float y, float z)
    {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    public void setPosition(Vector3f position)
    {
        this.position.x = position.x;
        this.position.y = position.y;
        this.position.z = position.z;
    }

    public Quaternionf getRotation()
    {
        return rotation;
    }

    public void setRotation(Vector3f rotation)
    {
        this.rotation.set(Conversions.convertEulerToQuaternion(rotation.x, rotation.y, rotation.z));
    }

    public void setRotation(float xRot, float yRot, float zRot)
    {
        this.rotation.set(Conversions.convertEulerToQuaternion(xRot, yRot, zRot));
    }

    public Vector3f getScale()
    {
        return scale;
    }

    public void setScale(Vector3f scale)
    {
        this.scale = scale;
    }

    public void setScale(float x, float y, float z)
    {
        scale.x = x;
        scale.y = y;
        scale.z = z;
    }

    public void setScale(float scale)
    {
        this.scale.x = scale;
        this.scale.y = scale;
        this.scale.z = scale;
    }

    public Mesh getMesh()
    {
        return mesh;
    }

    public void setMesh(Mesh mesh)
    {
        this.mesh = mesh;
    }

}
