package interfaces;


import input.MouseInput;

public interface IF_Game {

    void init() throws Exception;

    void start() throws Exception;

    void input(MouseInput mouseInput);

    void update(float deltaTime, MouseInput mouseInput);

    void render();

    void cleanup();
}
