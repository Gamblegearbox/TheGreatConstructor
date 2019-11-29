package interfaces;

import rendering.Mesh;
import rendering.ShaderProgram;
import rendering.Transform;

public interface IF_HudItem {

    Transform getTransform();

    Mesh getMesh();

    ShaderProgram getShader();

    void update(float _deltaTime);

    void cleanup();
}
