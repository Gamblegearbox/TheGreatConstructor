package oldEngineStuff.engine.interfaces;

import oldEngineStuff.engine.core.Window;
import oldEngineStuff.engine.input.MouseInput;

public interface IGameLogic {

    void init(Window window) throws Exception;

    void input(MouseInput mouseInput);

    void update(float interval, MouseInput mouseInput);

    void render();

    void cleanup();
}
