package com.ironqueue.util;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import com.ironqueue.core.Command;
import com.ironqueue.core.CommandHandler;
import com.ironqueue.job.Job;
import com.ironqueue.job.JobType;
import com.ironqueue.worker.WorkerInfo;

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
        StringBuilder sb = new StringBuilder();
        sb.append("--------IRONQUEUE HELP--------\n");
        int counter = 1;
        for (Map.Entry<String,Command> mapElement : CommandHandler.allCommands.entrySet()) {
            String name = mapElement.getKey();
            // Finding the value
            String des = mapElement.getValue().getDescription();
            sb.append(counter + ". " + name + " : " + des + "\n");
            counter++;
        }
        reader.printAbove(sb.toString());
        counter = 0;
    }
    public static void LogOneJob(Job job) {
        StringBuilder sb = new StringBuilder();
        sb.append("--------JOB DETAIL--------\n");
        sb.append("ID : " + job.getId().toString() + "\n");
        sb.append("TYPE : " + job.getType().toString() + "\n");
        sb.append("STATUS : " + job.getStatus().toString() + "\n");
        sb.append("ATTEMPTS : " + job.getAttempts() + "\n");
        reader.printAbove(sb.toString());
    }
    public static void LogJobs(List<Job> jobs) {
        StringBuilder sb = new StringBuilder();
        int count = 1;
        if(jobs.isEmpty()) {
            sb.append("--------NO JOBS FOUND--------\n");
        }
        else {
            sb.append("--------ALL JOBS--------\n");
            sb.append("#   ID                                    TYPE      STATUS\n");
            for (Job job : jobs) {
                sb.append(count +" "+ job.getId() + "    " + job.getType() + "    " + job.getStatus() + "\n");
                count++;
            }
        }
        reader.printAbove(sb.toString());
        count=0;
    }
    public static void LogJobTypes() {
        StringBuilder sb = new StringBuilder();
        sb.append("--------AVAILABLE JOB TYPES--------\n");
        for (JobType type : JobType.values()) {
            sb.append(type.toString() + "\n");
        }
        reader.printAbove(sb.toString());
    }
    public static void LogWorkers(List<WorkerInfo> workers) {
        int counter = 1;
        StringBuilder sb = new StringBuilder();
        if(workers.isEmpty()) {
            sb.append("--------NO WORKERS FOUND--------");
        }
        else {
            sb.append("--------ALL WORKERS--------\n");
            sb.append("#   ID                                    STATUS   JOBS COMPLETED\n");
            for (WorkerInfo worker : workers) {
                String status = (worker.isAlive() ? "ONLINE " : "OFFLINE");
                sb.append(counter + " " + worker.getWorkerId() + "    " + status + "       " + worker.getJobsProcessed() + "\n");
                counter++;
            }
        }
        reader.printAbove(sb.toString());
        counter=0;
    }
    public static void LogOutput(String msg) {
        reader.printAbove("--------OUTPUT--------");
        reader.printAbove(msg);
    }
    public static void LogError(String msg) {
        reader.printAbove("--------ERROR--------");
        reader.printAbove(msg);
        reader.printAbove("---------------------");
    }
    public static void LogError(StackTraceElement[] msg) {
        System.out.println(msg);
    }
    public static void SimpleLog(String s) {reader.printAbove(s);}
}