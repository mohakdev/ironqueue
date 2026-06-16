package com.ironqueue.core;

import java.util.ArrayList;
import java.util.List;

import com.ironqueue.util.Logger;
import com.ironqueue.worker.WorkerHandler;

public class CommandHandler {
    public static List<Command> allCommands = new ArrayList<>();

    public static void initializeCommands() {
        if(!allCommands.isEmpty()) {return;}
        Command worker = new Command("worker","Start Worker") {
            @Override
            public void execute(String[] args) {
                WorkerHandler.createWorker();
            }
        };
        Command createJob = new Command("create-job","Create a job") {
            @Override
            public void execute(String[] args) {
                WorkerHandler.createWorker();
            }
        };
        Command listWorker = new Command("workers", "List workers") {
            @Override
            public void execute(String[] args) {
                Logger.LogWorkers(WorkerHandler.listWorkers());
            }
        };
        allCommands.add(worker);
        allCommands.add(listWorker);
    }

    public static Command getCommandByName(String name) {
        for (Command command : allCommands) {
            if(command.getName().equals(name)){
                return command;
            }
        }
        return null;
    }
}
