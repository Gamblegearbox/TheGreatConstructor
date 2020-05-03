package core;

import utils.Logger;
import java.util.Scanner;

public class Console implements Runnable {

    private final Scanner scanner;

    public Console(){
        scanner = new Scanner(System.in);
    }

    @Override
    public void run() {
        startConsoleLoop();
    }

    private void startConsoleLoop()
    {
        Logger.getInstance().writeln("# STARTING CONSOLE LOOP");
        boolean isRunning = true;

        while(isRunning)
        {
            System.err.print("~");
            int value = scanner.nextInt();
            System.err.println("Value: " + value);

            if(value == -1){
                System.err.println("Goodbye");
                isRunning = false;
            }
        }

        Logger.getInstance().writeln("# CLOSING CONSOLE THREAD");
    }

}
