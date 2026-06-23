package com.ironqueue.util;

import java.io.IOException;
import java.util.List;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import com.ironqueue.core.Command;
import com.ironqueue.core.CommandHandler;
import com.ironqueue.worker.Worker;

public class Logger {
    public static boolean printLogs = false;
    public static LineReader reader;
    public static void InitializeLogger() throws IOException {
        Terminal terminal = TerminalBuilder.builder().system(true).build();
        reader = LineReaderBuilder.builder().terminal(terminal).build();
    }
    public static void Log(Class<?> caller,String msg) {
        if(!printLogs) {return;}
        if(caller == null) {
            reader.printAbove("<Main> " + msg);
        }
        else {
            String output = "<" + caller.getSimpleName() + "> " + msg;
            reader.printAbove(output);
        }
    }
    public static void LogHelp() {
        System.out.println("--------IRONQUEUE HELP--------");
        int counter = 1;
        for (Command command : CommandHandler.allCommands) {
            reader.printAbove(counter + ". " + command.getName() + " : " + command.getDescription());
            counter++;
        }
        counter = 0;
    }
    public static void LogWorkers(List<Worker> workers) {
        int counter = 1;
        System.out.println("--------ALL WORKERS--------");
        for (Worker worker : workers) {
            reader.printAbove(counter + ". worker:" + worker.getWorkerId());
            counter++;
        }
        counter=0;
    }
    public static void LogError(String msg) {
        reader.printAbove("--------ERROR--------");
        reader.printAbove(msg);
        reader.printAbove("---------------------");
    }
}