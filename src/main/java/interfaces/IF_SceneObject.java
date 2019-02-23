package interfaces;

import rendering.Mesh;
import rendering.Transform;

public interface IF_SceneObject {

    Transform getTransform();

    Mesh getMesh();

    void update(float _deltaTime);

    void cleanup();
}
