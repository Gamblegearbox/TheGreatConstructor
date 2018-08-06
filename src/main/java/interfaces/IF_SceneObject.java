package interfaces;

import engine.MeshAndTransform;

public interface IF_SceneObject {

    MeshAndTransform getMeshAndTransform();

    void update(float _deltaTime);
}
