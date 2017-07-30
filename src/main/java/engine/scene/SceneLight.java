package engine.scene;

import engine.light.DirectionalLight;
import org.joml.Vector3f;

public class SceneLight {

    private Vector3f ambientLight;
    private DirectionalLight directionalLight;

    public Vector3f getAmbientLight()
    {
        return ambientLight;
    }

    public void setAmbientLight(Vector3f ambientLight)
    {
        this.ambientLight = ambientLight;
    }

    public DirectionalLight getDirectionalLight()
    {
        return directionalLight;
    }

    public void setDirectionalLight(DirectionalLight directionalLight)
    {
        this.directionalLight = directionalLight;
    }
}
