package core;


public class Camera {

    private Transform transform;

    public Camera(){
        transform = new Transform();
    }


    public Transform getTransform(){
        return transform;
    }


}
