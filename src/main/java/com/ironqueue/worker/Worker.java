package com.ironqueue.worker;

import redis.clients.jedis.UnifiedJedis;
import com.ironqueue.queue.QueueService;
import com.ironqueue.storage.RedisStorage;
import com.ironqueue.executor.JobExecutor;
import com.ironqueue.executor.JobExecutorFactory;
import com.ironqueue.job.Job;
import com.ironqueue.util.Logger;
import java.util.UUID;

public class Worker {
    private final WorkerInfo metadata;
    private final UnifiedJedis jedis;
    private final QueueService queue;
    private final RedisStorage storage;
    private volatile boolean isRunning = true;

    public Worker() {
        this.metadata = new WorkerInfo();
        this.jedis = new UnifiedJedis("redis://localhost:6379");
        this.queue = new QueueService(jedis);
        this.storage = new RedisStorage(jedis);
    }

    public void start() throws Exception {
        Logger.Log(getClass(), "worker:"+metadata.getWorkerId() + " Started");
        storage.saveWorker(metadata);
        startHeartbeat();

        while(isRunning) {
            //Retrieving job from queue
            UUID jobId = queue.blockingDequeue(5);
            if(jobId == null) {System.out.println("No Jobs Found"); return;}
            processJob(jobId);
        }
    }

    public void processJob(UUID jobId) throws Exception {
        Job job = storage.getJob(jobId);
        job.incrementAttempts();
        storage.saveJob(job);
        try {
            job.markProcessing();
            storage.saveJob(job);
            Logger.Log(getClass(), "worker:"+ metadata.getWorkerId()+" is processing job:" + jobId);
            //Factory Pattern to execute jobs
            JobExecutor executor = JobExecutorFactory.getExecutor(job.getType());
            executor.execute(job);
            //Work Completed
            job.markCompleted();
            storage.saveJob(job);
            Logger.Log(getClass(), "worker:"+ metadata.getWorkerId()+" completed job:" + jobId);
        }
        catch (Exception e){
            if(job.canRetry()) {
                // Put back into queue
                job.markPending();
                storage.saveJob(job);
                queue.enqueue(job.getId());
            } else {
                // Permanently failed
                job.markFailed();
                storage.saveJob(job);
                Logger.Log(getClass(), "worker:"+ metadata.getWorkerId()+" failed job:" + jobId);
                System.out.println(e);
            }
        }
    }

    public void startHeartbeat() {
        Thread heartbeatThread = new Thread(() -> {
            while (true) {
                try {
                    metadata.heartbeat();
                    storage.saveWorker(metadata);
                    Logger.Log(getClass(),"Heartbeat sent by Worker:"+metadata.getWorkerId());
                    Thread.sleep(5000);
                } catch (Exception e) {
                    Logger.Log(getClass(), "Stopping Heartbeats of Worker:"+metadata.getWorkerId());
                    return;
                }
            }
        });

        heartbeatThread.setDaemon(true);
        heartbeatThread.start();
    }
    public void shutdown(){
        Logger.Log(getClass(),"worker:" + metadata.getWorkerId() + " shutting down...");
        isRunning = false;
    }
}