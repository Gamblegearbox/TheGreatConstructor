import core.CoreLoop;
import core.EngineOptions;
import utils.Logger;

import java.io.File;

public class Main {

    public static void main(String[] args)
    {
        try
        {
            String dir = System.getProperty("user.dir");

            EngineOptions.loadSettingFromFile("./res/config/engineOptions.ini");

            Logger.getInstance().writeln("> SETTINGS LOADED FROM CONFIG FILE");
            EngineOptions.logOptionsStatus();

            Logger.getInstance().writeln("> STARTING PROGRAM");
            logSystemInfo();

            CoreLoop coreLoop = new CoreLoop();
            coreLoop.start();
        }
        catch(Exception e)
        {
            System.out.println("Could not find config file!");
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
