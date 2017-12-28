package interfaces;


public interface InterfaceGame {

    void init() throws Exception;

    void start() throws Exception;

    void input(float deltaTime) throws Exception;

    void update(float deltaTime);

    void render();

    void cleanup();
}
