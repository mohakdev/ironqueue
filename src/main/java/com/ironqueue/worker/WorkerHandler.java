package com.ironqueue.worker;

import java.util.ArrayList;
import java.util.List;

import com.ironqueue.util.Logger;

public class WorkerHandler {
    private static List<Worker> workers = new ArrayList<>();

    public static void createWorker() {
        Worker worker = new Worker();
        Runtime.getRuntime().addShutdownHook(
            new Thread(() -> worker.shutdown())
        );
        new Thread(() -> {
            try {
                worker.start();
            } catch (Exception e) {
                Logger.LogError("Unable to start worker");
                return;
            }
        }).start();
        workers.add(worker);
    }
    public static void shutAllWorkers() {
        for(Worker worker : workers) {
            worker.shutdown();
        }
    }
}