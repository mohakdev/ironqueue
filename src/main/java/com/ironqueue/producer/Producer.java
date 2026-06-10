package com.ironqueue.producer;

import com.ironqueue.job.Job;
import com.ironqueue.queue.QueueService;
import com.ironqueue.storage.RedisStorage;

public class Producer {

    private final RedisStorage storage;
    private final QueueService queue;

    public Producer() {
        this.storage = new RedisStorage();
        this.queue = new QueueService();
    }

    public void submit(Job job) {
        storage.saveJob(job);
        queue.enqueue(job.getId());
    }
}