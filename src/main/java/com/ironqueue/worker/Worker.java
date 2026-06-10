package com.ironqueue.worker;

import com.ironqueue.queue.QueueService;
import com.ironqueue.storage.RedisStorage;
import com.ironqueue.job.Job;
import com.ironqueue.job.JobStatus;
import java.util.UUID;

public class Worker {
    private final QueueService queue;
    private final RedisStorage storage;

    public Worker() {
        this.queue = new QueueService();
        this.storage = new RedisStorage();
    }

    public void processNextJob() throws Exception {
        UUID jobId = queue.dequeue();
        if(jobId == null) {System.out.println("No Jobs Found"); return;}
        Job job = storage.getJob(jobId);
        try {
            job.setStatus(JobStatus.PROCESSING);
            storage.saveJob(job);
            //Actual Work
            Thread.sleep(3000);
            //Work Completed
            job.setStatus(JobStatus.COMPLETED);
            storage.saveJob(job);
        }
        catch (Exception e){
            job.setStatus(JobStatus.FAILED);
            storage.saveJob(job);
            System.out.println(e);
        }
    }
}