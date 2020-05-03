package rendering;

import cameras.SimpleCamera;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transformation {

    private final Matrix4f projectionMatrixPerspective;
    private final Matrix4f projectionMatrixOrthographic;
    private final Matrix4f viewMatrix;
    private final Matrix4f viewProjectionMatrix;

    private final Matrix4f modelViewMatrix;
    private final Matrix4f modelMatrix;

    public Transformation(){
        projectionMatrixPerspective = new Matrix4f();
        projectionMatrixOrthographic = new Matrix4f();
        viewMatrix = new Matrix4f();
        viewProjectionMatrix = new Matrix4f();

        modelViewMatrix = new Matrix4f();
        modelMatrix = new Matrix4f();
    }

    public final Matrix4f getPerspectiveProjectionMatrix(float fov, float width, float height, float zNear, float zFar) {
        float aspectRatio = width / height;
        projectionMatrixPerspective.identity();
        projectionMatrixPerspective.perspective((float) Math.toRadians(fov), aspectRatio, zNear, zFar);
        return projectionMatrixPerspective;
    }

    public Matrix4f getOrthographicProjectionMatrix(float left, float right, float bottom, float top) {
        projectionMatrixOrthographic.identity();
        projectionMatrixOrthographic.setOrtho2D(left, right, bottom, top);

        return projectionMatrixOrthographic;
    }

    public Matrix4f getViewProjectionMatrix(){
        projectionMatrixPerspective.mul(viewMatrix, viewProjectionMatrix);

        return viewProjectionMatrix;
    }

    public Matrix4f getModelViewMatrix(Transform gameItem, Matrix4f viewMatrix) {
        Vector3f rotation = gameItem.getRotation();
        modelViewMatrix.identity().translate(gameItem.getPosition()).
                rotateX((float)Math.toRadians(-rotation.x)).
                rotateY((float)Math.toRadians(-rotation.y)).
                rotateZ((float)Math.toRadians(-rotation.z)).
                scale(gameItem.getScale());
        Matrix4f viewCurr = new Matrix4f(viewMatrix);
        return viewCurr.mul(modelViewMatrix);
    }

    public Matrix4f getModelMatrix(Transform gameItem) {
        Vector3f rotation = gameItem.getRotation();
        modelMatrix.identity().translate(gameItem.getPosition()).
                rotateX((float)Math.toRadians(-rotation.x)).
                rotateY((float)Math.toRadians(-rotation.y)).
                rotateZ((float)Math.toRadians(-rotation.z)).
                scale(gameItem.getScale());

        return modelMatrix;
    }

    public Matrix4f getViewMatrix(SimpleCamera camera) {
        Vector3f cameraPos = camera.getPosition();
        Vector3f rotation = camera.getRotation();

        viewMatrix.identity();
        // First do the rotation so camera rotates over its position
        viewMatrix.rotate((float)Math.toRadians(rotation.x), new Vector3f(1, 0, 0))
                .rotate((float)Math.toRadians(rotation.y), new Vector3f(0, 1, 0));
        // Then do the translation
        viewMatrix.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        return viewMatrix;
    }


}
