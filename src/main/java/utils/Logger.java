package utils;

import java.util.HashMap;
import java.util.Map;

public class Logger {

    private static Logger instance;
    private final Map<String, Integer> logs;

    private Logger()
    {
        logs = new HashMap<>();
    }

    public static Logger getInstance()
    {
        if(instance == null)
        {
            instance = new Logger();
        }

        return instance;
    }

    public void log(String _label, int _logValue)
    {
        logs.put(_label, _logValue);
    }


    public void printAll()
    {
        for(Map.Entry e : logs.entrySet())
        {
            System.out.print(e.getKey() + ": " + e.getValue() + " | ");
        }
        System.out.println();
    }

}
