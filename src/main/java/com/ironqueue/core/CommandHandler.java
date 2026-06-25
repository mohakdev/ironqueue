package com.ironqueue.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


import com.ironqueue.job.Job;
import com.ironqueue.job.JobType;
import com.ironqueue.producer.Producer;
import com.ironqueue.storage.RedisStorage;
import com.ironqueue.util.Logger;
import com.ironqueue.worker.WorkerHandler;
import com.ironqueue.worker.WorkerInfo;

import redis.clients.jedis.UnifiedJedis;

public class CommandHandler {
    public static Map<String,Command> allCommands = new HashMap<>();
    private static UnifiedJedis jedis;
    private static RedisStorage storage;
    static Producer producer = new Producer();

    public static void initializeCommands() {
        jedis = new UnifiedJedis("redis://localhost:6379");
        storage = new RedisStorage(jedis);
        if(!allCommands.isEmpty()) {return;}

        //All Commands
        Command createJob = new Command("Create a job") {
            @Override
            public void execute(String[] args) throws Exception {
                Map<String,Object> payload = new HashMap<String,Object>();
                JobType jobType = JobType.valueOf(args[1].toUpperCase().trim());
                //Add Payload if needed
                for(int i=2; i<args.length; i++) {
                    payload.put("arg"+i, args[i]);
                }
                Job jobToExecute = new Job(jobType,payload);
                producer.submit(jobToExecute);
            }
        };
        Command viewJob = new Command("Info about a particular job") {
            @Override
            public void execute(String[] args) throws Exception {
                UUID jobId = UUID.fromString(args[1]);
                Job job = storage.getJob(jobId);
                Logger.LogOneJob(job);
            }
        };
        Command viewAllJobs = new Command("Lists all jobs in database") {
            @Override
            public void execute(String[] args) throws Exception {
                Set<String> allJobId = storage.getAllJobIds();
                List<Job> allJobs = new ArrayList<>();

                for (String id : allJobId) {
                    allJobs.add(storage.getJob(UUID.fromString(id)));
                }
                Logger.LogJobs(allJobs);
            }
        };
        Command worker = new Command("Start Worker") {
            @Override
            public void execute(String[] args) {
                WorkerHandler.createWorker();
            }
        };
        Command listWorker = new Command("List workers") {
            @Override
            public void execute(String[] args) {
                try {
                    Set<String> allWorkersId = storage.getAllWorkerIds();
                    List<WorkerInfo> allWorkers = new ArrayList<>();
                    for (String id : allWorkersId) {
                        allWorkers.add(storage.getWorker(UUID.fromString(id)));
                    }
                    Logger.LogWorkers(allWorkers);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        Command help = new Command("Shows all commands with their functions") {
            @Override
            public void execute(String[] args) {
                Logger.LogHelp();
            }
        };
        Command logs = new Command("Can be turned on or off to see background logs. use `logs on` to turn on logs and `logs off` to turn them off.") {
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
        Command exit = new Command("Exit the program") {
            @Override
            public void execute(String[] args) {
                IronqueueCLI.keepRunning = false;
                WorkerHandler.shutAllWorkers();
            }
        };

        allCommands.put("job-create",createJob);
        allCommands.put("job-info", viewJob);
        allCommands.put("job-list",viewAllJobs);
        allCommands.put("worker-start",worker);
        allCommands.put("worker-list",listWorker);
        allCommands.put("logs",logs);
        allCommands.put("help",help);
        allCommands.put("exit",exit);
    }

    public static Command getCommandByName(String name) {
        return allCommands.get(name);
    }
}
