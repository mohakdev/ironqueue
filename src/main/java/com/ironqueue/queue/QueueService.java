package com.ironqueue.queue;

import redis.clients.jedis.UnifiedJedis;
import java.util.UUID;

public class QueueService {
    private final UnifiedJedis jedis;
    public QueueService() {
        this.jedis = new UnifiedJedis("redis://localhost:6379");
    };
    public void enqueue(UUID jobId) {
        jedis.lpush("jobs",jobId);
    }
    public UUID dequeue() {
        String id = jedis.rpop("jobs");
        if(id == null) {return null;}
        return UUID.fromString(id);
    }
}