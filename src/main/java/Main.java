import core.CoreLoop;
import core.EngineOptions;
import utils.Logger;

public class Main {

    public static void main(String[] args)
    {
        Logger.getInstance().writeln("[" + Main.class.getName() + "] system info:");
        logSystemInfo();

        CoreLoop coreLoop = new CoreLoop();
        coreLoop.start();
    }

    public static void logSystemInfo()
    {
        String info =
                "\tOPERATING SYSTEM:          " + EngineOptions.OPERATING_SYSTEM + "\n" +
                "\tVERSION:                   " + System.getProperty("os.version") + "\n" +
                "\tARCHITECTURE:              " + System.getProperty("os.arch") + "\n" +
                "\n";

        Logger.getInstance().write(info);
    }
}
