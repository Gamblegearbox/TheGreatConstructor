package engine.core;

import engine.camera.Camera;
import engine.gameEntities.GameEntity;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Transformation {

    private final Matrix4f projectionMatrix;
    private final Matrix4f modelMatrix;
    private final Matrix4f modelViewMatrix;
    private final Matrix4f viewMatrix;
    private final Matrix4f ortho2DMatrix;
    private final Matrix4f orthoModelMatrix;


    public Transformation()
    {
        projectionMatrix = new Matrix4f();
        modelMatrix = new Matrix4f();
        modelViewMatrix = new Matrix4f();
        viewMatrix = new Matrix4f();
        ortho2DMatrix = new Matrix4f();
        orthoModelMatrix = new Matrix4f();
    }

    public Matrix4f getProjectionMatrix()
    {
        return projectionMatrix;
    }

    public Matrix4f updateProjectionMatrix(float fov, float width, float height, float zNear, float zFar)
    {
        float aspectRatio = width / height;
        projectionMatrix.setPerspective(fov, aspectRatio, zNear, zFar);

        return projectionMatrix;
    }

    public Matrix4f getViewMatrix()
    {
        return viewMatrix;
    }

    public Matrix4f updateViewMatrix(Camera camera)
    {
        return updateGenericViewMatrix(camera.getPosition(), camera.getRotation(), viewMatrix);
    }

    public static  Matrix4f updateGenericViewMatrix(Vector3f position, Vector3f rotation, Matrix4f matrix) {
        // First do the rotation so camera rotates over its position
        return matrix.rotationX((float)Math.toRadians(rotation.x))
                .rotateY((float)Math.toRadians(rotation.y))
                .translate(-position.x, -position.y, -position.z);
    }

    public final Matrix4f getOrtho2DProjectionMatrix(float left, float right, float bottom, float top)
    {
        ortho2DMatrix.identity();
        ortho2DMatrix.setOrtho2D(left, right, bottom, top);

        return ortho2DMatrix;
    }

    public Matrix4f buildModelMatrix(GameEntity gameEntity)
    {
        /*Vector3f rotation = gameEntity.getRotation();
        return modelMatrix.identity().translate(gameEntity.getPosition()).
                rotateX((float)Math.toRadians(-rotation.x)).
                rotateY((float)Math.toRadians(-rotation.y)).
                rotateZ((float)Math.toRadians(-rotation.z)).
                scale(gameEntity.getScale());*/
        Quaternionf rotation = gameEntity.getRotation();
        Vector3f scale = gameEntity.getScale();
        return modelMatrix.translationRotateScale(
                gameEntity.getPosition().x, gameEntity.getPosition().y, gameEntity.getPosition().z,
                rotation.x, rotation.y, rotation.z, rotation.w,
                scale.x, scale.y, scale.z);
    }

    public Matrix4f buildModelViewMatrix(Matrix4f modelMatrix, Matrix4f viewMatrix)
    {
        return viewMatrix.mulAffine(modelMatrix, modelViewMatrix);
    }

    public Matrix4f buildOrthoProjModelMatrix(GameEntity gameEntity, Matrix4f orthoMatrix)
    {
        /*Vector3f rotation = gameEntity.getRotation();
        modelMatrix.identity().translate(gameEntity.getPosition()).
                rotateX((float) Math.toRadians(-rotation.x)).
                rotateY((float) Math.toRadians(-rotation.y)).
                rotateZ((float) Math.toRadians(-rotation.z)).
                scale(gameEntity.getScale());*/

        Quaternionf rotation = gameEntity.getRotation();
        Vector3f scale = gameEntity.getScale();
        modelMatrix.translationRotateScale(
                gameEntity.getPosition().x, gameEntity.getPosition().y, gameEntity.getPosition().z,
                rotation.x, rotation.y, rotation.z, rotation.w,
                scale.x, scale.y, scale.z);

        orthoModelMatrix.set(orthoMatrix);
        orthoModelMatrix.mul(modelMatrix);

        return orthoModelMatrix;
    }
}
