package com.ironqueue.worker;

import com.ironqueue.util.Logger;

public class WorkerHandler {

    public static void main(String[] args) {

        Worker worker = new Worker();
        Runtime.getRuntime().addShutdownHook(
            new Thread(() -> worker.shutdown())
        );
        try {
            worker.start();
        }
        catch (Exception e) {
            Logger.Log(null, "Unable to start worker");
        }
    }
}