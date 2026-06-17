package com.ironqueue.core;

import java.util.Scanner;
import com.ironqueue.util.Logger;

public class IronqueueCLI {
    public static boolean keepRunning = true;
    public static void main(String[] args) {
        CommandHandler.initializeCommands();
        Logger.LogWelcome();
        Scanner sc = new Scanner(System.in);
        while(keepRunning) {
            System.out.print("IRONQUEUE > ");
            String inputLine = sc.nextLine();
            String[] params = inputLine.split("\\s+");
            try {
                Command commandToRun = CommandHandler.getCommandByName(params[0]);
                commandToRun.execute(params);
            }
            catch (Exception e) {
                Logger.Log(null, "Unable to run the desired command");
            }
        }
        sc.close();
    }
}