package utils;

import core.EngineOptions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Logger {

    private static Logger instance;

    private BufferedWriter out;
    private final Map<String, Integer> logs;

    private Logger()
    {
        logs = new HashMap<>();

        if (EngineOptions.LOG_TO_FILE)
        {
            File logFile = new File("EngineLogFile.txt");

            try
            {
                if (!logFile.exists())
                {
                    logFile.createNewFile();
                }

                out = new BufferedWriter(new FileWriter(logFile, true));
                out.write("ENGINE START: " + Calendar.getInstance().getTime() + "\n\n");
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public static Logger getInstance()
    {
        if(instance == null)
        {
            instance = new Logger();
        }

        return instance;
    }

    public void logData(String _label, int _logValue)
    {
        logs.put(_label, _logValue);
    }

    public void write(String _log)
    {
        if (EngineOptions.LOG_TO_FILE)
        {
            try {
                out.write(_log);
                out.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
        {
            System.out.print(_log);
        }
    }

    public void writeln(String _log)
    {
        if (EngineOptions.LOG_TO_FILE)
        {
            try
            {
                out.write(_log + "\n");
                out.flush();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            System.out.println(_log);
        }
    }

    public void outputLoggedData()
    {
        if (EngineOptions.LOG_TO_FILE)
        {
            try
            {
                for (Map.Entry e : logs.entrySet())
                {
                    out.write(e.getKey() + ": " + e.getValue() + " | ");
                }
                out.write("\n");
                out.flush();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            for(Map.Entry e : logs.entrySet())
            {
                System.out.print(e.getKey() + ": " + e.getValue() + " | ");
            }
            System.out.println();
        }
    }

    public void cleanup()
    {
        if (EngineOptions.LOG_TO_FILE)
        {
            try {
                out.write("\n");
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
