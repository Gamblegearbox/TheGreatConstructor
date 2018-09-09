package interfaces;

import engine.OpenGLMesh;
import engine.Transform;

public interface IF_SceneObject {

    Transform getTransform();

    OpenGLMesh getMesh();

    void update(float _deltaTime);

    void cleanup();
}
