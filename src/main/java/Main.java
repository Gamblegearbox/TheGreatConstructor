import engine.CoreLoop;
import utils.Logger;

public class Main {

    public static void main(String[] args)
    {
        Logger.getInstance().writeTolog("> STARTING MAIN\n");
        CoreLoop coreLoop = new CoreLoop();
        coreLoop.start();
    }
}
