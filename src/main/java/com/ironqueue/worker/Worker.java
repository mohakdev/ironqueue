package com.ironqueue.worker;

import redis.clients.jedis.UnifiedJedis;
import com.ironqueue.queue.QueueService;
import com.ironqueue.storage.RedisStorage;
import com.ironqueue.executor.JobExecutor;
import com.ironqueue.executor.JobExecutorFactory;
import com.ironqueue.job.Job;
import com.ironqueue.job.JobStatus;
import com.ironqueue.util.Logger;
import java.util.UUID;

public class Worker {
    private final WorkerInfo metadata;
    private final UnifiedJedis jedis;
    private final QueueService queue;
    private final RedisStorage storage;

    public Worker() {
        metadata = new WorkerInfo();
        this.jedis = new UnifiedJedis("redis://localhost:6379");
        this.queue = new QueueService(jedis);
        this.storage = new RedisStorage(jedis);
    }
    public void start() throws Exception {
        Logger.Log(getClass(), metadata.getWorkerId() + " Started");
        storage.saveWorker(metadata);
        while(true) {
            processNextJob();
        }
    }

    public void processNextJob() throws Exception {
        UUID jobId = queue.blockingDequeue();
        if(jobId == null) {System.out.println("No Jobs Found"); return;}
        Job job = storage.getJob(jobId);
        try {
            job.setStatus(JobStatus.PROCESSING);
            storage.saveJob(job);
            Logger.Log(getClass(), "is processing job:" + jobId);
            //Factory Pattern to execute jobs
            JobExecutor executor = JobExecutorFactory.getExecutor(job.getType());
            executor.execute(job);
            //Work Completed
            job.setStatus(JobStatus.COMPLETED);
            storage.saveJob(job);
            Logger.Log(getClass(), "completed job:" + jobId);
        }
        catch (Exception e){
            job.setStatus(JobStatus.FAILED);
            storage.saveJob(job);
            Logger.Log(getClass(), "failed job:" + jobId);
            System.out.println(e);
        }
    }
}