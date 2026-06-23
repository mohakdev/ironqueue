package com.ironqueue.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.relation.Role;

import com.ironqueue.job.Job;
import com.ironqueue.job.JobType;
import com.ironqueue.producer.Producer;
import com.ironqueue.util.Logger;
import com.ironqueue.worker.WorkerHandler;

public class CommandHandler {
    public static List<Command> allCommands = new ArrayList<>();
    static Producer producer = new Producer();

    public static void initializeCommands() {
        if(!allCommands.isEmpty()) {return;}
        Command createJob = new Command("job-create","Create a job") {
            @Override
            public void execute(String[] args) throws Exception {
                Map<String,Object> payload = new HashMap<String,Object>();

                //Add Payload if needed
                for(int i=2; i<args.length; i++) {
                    payload.put("arg"+i, args[i]);
                }
                JobType jobType = JobType.valueOf(args[1].toUpperCase().trim());
                Job jobToExecute = new Job(jobType,payload);
                producer.submit(jobToExecute);
            }
        };
        //TODO: Complete these 2 commands
        // Command viewJob = new Command("job-info","Info about a particular job") {
        //     @Override
        //     public void execute(String[] args) throws Exception {
                
        //     }
        // };
        // Command viewAllJobs = new Command("jobs","Lists all jobs in database") {
        //     @Override
        //     public void execute(String[] args) throws Exception {
                
        //     }
        // };
        Command worker = new Command("worker-start","Start Worker") {
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
        Command help = new Command("help","Shows all commands with their functions") {
            @Override
            public void execute(String[] args) {
                Logger.LogHelp();
            }
        };
        Command logs = new Command("logs","Can be turned on or off to see background logs. \nuse `logs on` to turn on logs and `logs off` to turn them off.") {
            @Override
            public void execute(String[] args) {
                if(args[1].equals("on")) {
                    Logger.printLogs = true;
                }
                else if (args[1].equals("off")) {
                    Logger.printLogs = false;
                }
                else {
                    Logger.LogError("Invalid Parameter passed in a command.");
                }
            }
        };
        Command exit = new Command("exit","Exit the program") {
            @Override
            public void execute(String[] args) {
                IronqueueCLI.keepRunning = false;
            }
        };
        allCommands.add(createJob);
        allCommands.add(worker);
        allCommands.add(listWorker);
        allCommands.add(help);
        allCommands.add(logs);
        allCommands.add(exit);
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
