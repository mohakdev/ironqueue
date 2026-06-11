package com.ironqueue.producer;

import redis.clients.jedis.UnifiedJedis;
import com.ironqueue.job.Job;
import com.ironqueue.queue.QueueService;
import com.ironqueue.storage.RedisStorage;

public class Producer {
    private final UnifiedJedis jedis;
    private final RedisStorage storage;
    private final QueueService queue;

    public Producer() {
        this.jedis = new UnifiedJedis("redis://localhost:6379");
        this.storage = new RedisStorage(jedis);
        this.queue = new QueueService(jedis);
    }

    public void submit(Job job) throws Exception {
        storage.saveJob(job);
        queue.enqueue(job.getId());
    }
}