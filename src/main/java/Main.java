import com.sun.org.apache.xpath.internal.SourceTree;
import engine.CoreLoop;
import engine.EngineOptions;
import utils.Logger;

public class Main {

    public static void main(String[] args)
    {
        try
        {
            EngineOptions.loadSettingFromFile("/config/engineOptions.ini");

            Logger.getInstance().writeln("> SETTINGS LOADED FROM CONFIG FILE");
            EngineOptions.logOptionsStatus();

            Logger.getInstance().writeln("> STARTING PROGRAM");
            EngineOptions.logSystemInfo();

            CoreLoop coreLoop = new CoreLoop();
            coreLoop.start();
        }
        catch(Exception e)
        {
            System.out.println("Could not find config file!");
            System.exit(-1);
        }



    }
}
