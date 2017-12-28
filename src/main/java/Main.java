import engine.CoreLoop;
import utils.Logger;

public class Main {

    public static void main(String[] args)
    {
        Logger.getInstance().writeln("> STARTING MAIN");
        CoreLoop coreLoop = new CoreLoop();
        coreLoop.start();
    }
}
