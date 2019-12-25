package interfaces;

import rendering.Mesh;
import rendering.ShaderProgram;
import rendering.Transform;

public interface IF_SceneItem{

    Transform getTransform();

    Mesh getMesh();

    ShaderProgram getShader();

    void setDistanceToCamera(float _distance);

    float getDistanceToCamera();

    void setIlluminationAmount(float _glow);

    float getIlluminationAmount();

    void setOpacity(float _opacity);

    float getOpacity();

    void update(float _deltaTime);

    void cleanup();
}
