package interfaces;

import rendering.Mesh;
import rendering.ShaderProgram;
import rendering.Transform;

public interface IF_SceneItem {

    Transform getTransform();

    Mesh getMesh();

    ShaderProgram getShader();

    float getIlluminationAmount();

    void setIlluminationAmount(float _glow);

    void update(float _deltaTime);

    void cleanup();
}
