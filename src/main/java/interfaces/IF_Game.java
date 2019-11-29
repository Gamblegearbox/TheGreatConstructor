package interfaces;


import input.MouseInput;

public interface IF_Game {

    void init() throws Exception;

    void start();

    void update(float _deltaTime, MouseInput _mouseInput);

    void render(float _deltaTime);

    void cleanup();
}
