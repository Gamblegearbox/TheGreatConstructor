package interfaces;


public interface InterfaceGame {

    void init() throws Exception;

    void input(float deltaTime);

    void update(float deltaTime);

    void render();

    void cleanup();
}
