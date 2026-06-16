package com.ironqueue.util;

import java.util.List;

import com.ironqueue.core.Command;
import com.ironqueue.core.CommandHandler;
import com.ironqueue.worker.Worker;

public class Logger {
    public static boolean printLogs = true;
    public static void Log(Class<?> caller,String msg) {
        if(!printLogs) {return;}
        if(caller == null) {
            System.out.println("<Main> " + msg);
        }
        else {
            System.out.println("<" + caller.getSimpleName() + "> " + msg);
        }
    }
    public static void LogHelp() {
        System.out.println("--------IRONQUEUE HELP--------");
        int counter = 1;
        for (Command command : CommandHandler.allCommands) {
            System.out.println(counter + ". " + command.getName() + " : " + command.getDescription());
            counter++;
        }
        counter = 0;
    }
    public static void LogWorkers(List<Worker> workers) {
        int counter = 1;
        System.out.println("--------ALL WORKERS--------");
        for (Worker worker : workers) {
            System.out.println(counter + ". worker:" + worker.getWorkerId());
            counter++;
        }
        counter=0;
    }
}