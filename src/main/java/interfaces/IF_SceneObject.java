package interfaces;

import rendering.Material;
import rendering.OpenGLMesh;
import core.Transform;

public interface IF_SceneObject {

    Transform getTransform();

    OpenGLMesh getMesh();

    void update(float _deltaTime);

    void cleanup();
}
