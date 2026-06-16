package com.ironqueue.core;


import com.ironqueue.util.Logger;

public class IronqueueCLI {
    public static void main(String[] args) {
        if(args.length == 0) {Logger.LogHelp(); return;}
        String commandName = args[0];

        CommandHandler.initializeCommands();
        Command commandToRun = CommandHandler.getCommandByName(commandName);
        //System.out.println(commandToRun.getName());
        commandToRun.execute(args);
    }
}