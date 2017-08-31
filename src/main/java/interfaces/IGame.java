package interfaces;

import engineCore.Window;

public interface IGame {

    void init();

    void input();

    void update(float deltaTime);

    void render();

    void cleanup();
}
