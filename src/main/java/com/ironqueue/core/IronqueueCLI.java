package com.ironqueue.core;


import com.ironqueue.util.Logger;

public class IronqueueCLI {
    public static boolean keepRunning = true;
    public static void main(String[] args) {
        CommandHandler.initializeCommands();

        try { 
            Logger.InitializeLogger(); 
        }
        catch(Exception e){
            Logger.LogError(e.toString());
        }

        System.out.println("--------WELCOME TO IRONQUEUE--------");
        while(keepRunning) {
            String inputLine = Logger.reader.readLine("IRONQUEUE > ");
            String[] params = inputLine.split("\\s+");
            try {
                Command commandToRun = CommandHandler.getCommandByName(params[0]);
                commandToRun.execute(params);
            }
            catch (Exception e) {
                Logger.LogError("Unable to run the desired command");
            }
        }
    }
}