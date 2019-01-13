import core.CoreLoop;
import core.EngineOptions;
import utils.Logger;

import java.io.File;

public class Main {

    public static void main(String[] args)
    {
        String dir = System.getProperty("user.dir");
        String path = "./res/config/engineOptions.ini";

        try
        {
            EngineOptions.loadSettingFromFile(path);

            Logger.getInstance().writeln("[" + Main.class.getName() + "] system info:");
            logSystemInfo();

            Logger.getInstance().writeln("[" + Main.class.getName() + "] current setting:");
            EngineOptions.logOptionsStatus();


            CoreLoop coreLoop = new CoreLoop();
            coreLoop.start();
        }
        catch(Exception e)
        {
            Logger.getInstance().writeln("[" + Main.class.getName() + "] could not load config file under:" + path + " [closing application]");
            System.exit(-1);
        }
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
