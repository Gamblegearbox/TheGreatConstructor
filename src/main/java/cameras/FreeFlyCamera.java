package cameras;

import input.KeyboardInput;
import input.MouseInput;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class FreeFlyCamera {

    private final Vector3f position;
    private final Vector3f rotation;

    private float fov;
    private final float sensitivity;
    private final float speed;

    public FreeFlyCamera(float _fov, float _sensitivity, float _speed){
        position = new Vector3f(0,0,0);
        rotation = new Vector3f(0,0,0);
        fov = _fov;
        sensitivity = _sensitivity;
        speed = _speed;
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

    public void movePosition(float offsetX, float offsetY, float offsetZ) {
        if ( offsetZ != 0 ) {
            position.x += (float)Math.sin(Math.toRadians(rotation.y)) * -1.0f * offsetZ;
            position.z += (float)Math.cos(Math.toRadians(rotation.y)) * offsetZ;
        }
        if ( offsetX != 0) {
            position.x += (float)Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * offsetX;
            position.z += (float)Math.cos(Math.toRadians(rotation.y - 90)) * offsetX;
        }
        position.y += offsetY;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(float x, float y, float z) {
        rotation.x = x;
        rotation.y = y;
        rotation.z = z;
    }

    public void moveRotation(float offsetX, float offsetY, float offsetZ) {
        rotation.x += offsetX;
        rotation.y += offsetY;
        rotation.z += offsetZ;
    }

    final Vector3f cameraInc = new Vector3f(0,0,0);
    public void update(float _deltaTime, MouseInput _mouseInput){
        // POSITION

        cameraInc.set(0, 0, 0);
        if (KeyboardInput.isKeyRepeated(GLFW_KEY_W)) {
            cameraInc.z = -speed * _deltaTime;
        }
        else if (KeyboardInput.isKeyRepeated(GLFW_KEY_S)) {
            cameraInc.z = speed * _deltaTime;
        }
        if (KeyboardInput.isKeyRepeated(GLFW_KEY_A)) {
            cameraInc.x = -speed * _deltaTime;
        }
        else if (KeyboardInput.isKeyRepeated(GLFW_KEY_D)) {
            cameraInc.x = speed * _deltaTime;
        }
        if (KeyboardInput.isKeyRepeated(GLFW_KEY_Q)) {
            cameraInc.y = -speed * _deltaTime;
        }
        else if (KeyboardInput.isKeyRepeated(GLFW_KEY_E)) {
            cameraInc.y = speed * _deltaTime;
        }

        movePosition(cameraInc.x,cameraInc.y,cameraInc.z);

        // ROTATION
        if (_mouseInput.isRightButtonPressed()) {
            Vector2f rotVec = _mouseInput.getDisplVec();
            moveRotation(rotVec.x * sensitivity, rotVec.y * sensitivity, 0);
        }

    }
}
