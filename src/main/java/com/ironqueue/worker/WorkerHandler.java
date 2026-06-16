package com.ironqueue.worker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ironqueue.util.Logger;

public class WorkerHandler {
    private static List<Worker> workers = new ArrayList<>();

    public static void createWorker() {
        Worker worker = new Worker();
        Runtime.getRuntime().addShutdownHook(
            new Thread(() -> worker.shutdown())
        );
        try {
            worker.start();
        }
        catch (Exception e) {
            Logger.Log(null, "Unable to start worker");
            return;
        }
        workers.add(worker);
    }
    public static List<Worker> listWorkers() {
        return workers;
    }
}