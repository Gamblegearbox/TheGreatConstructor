package cameras;

import org.joml.Vector3f;

public class SimpleCamera {

    private final Vector3f position;
    private final Vector3f rotation;

    private float fov;

    public SimpleCamera(float _fov){
        position = new Vector3f(0,0,0);
        rotation = new Vector3f(0,0,0);
        fov = _fov;
    }


    public float getFieldOfView(){
        return fov;
    }

    public void setFieldOfView(float _fov){
        fov = _fov;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(float x, float y, float z) {
        position.x = x;
        position.y = y;
        position.z = z;
    }

    public void setPosition(Vector3f _pos) {
        position.x = _pos.x;
        position.y = _pos.y;
        position.z = _pos.z;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(float x, float y, float z) {
        rotation.x = x;
        rotation.y = y;
        rotation.z = z;
    }

}
