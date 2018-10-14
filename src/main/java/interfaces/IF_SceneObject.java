package interfaces;

import rendering.OpenGLMesh;
import core.Transform;

public interface IF_SceneObject {

    Transform getTransform();

    OpenGLMesh getMesh();

    void update(float _deltaTime);

    void cleanup();
}
