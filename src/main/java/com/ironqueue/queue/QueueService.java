package com.ironqueue.queue;

import redis.clients.jedis.UnifiedJedis;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.ironqueue.util.Logger;

public class QueueService {
    private final UnifiedJedis jedis;
    public QueueService(UnifiedJedis jedis) {
        this.jedis = jedis;
    };
    public void enqueue(UUID jobId) {
        jedis.lpush("jobs",jobId.toString());
        Logger.Log(getClass(), "Job:" + jobId + " queued");
    }
    public UUID dequeue() {
        String id = jedis.rpop("jobs");
        if(id == null) {
            Logger.Log(getClass(), "No job found in queue");
            return null;
        }
        Logger.Log(getClass(), "Returning job:" + id + " from queue");
        return UUID.fromString(id);
    }
    public void enqueueDeadLetter(UUID jobId) {
        jedis.lpush("dead_jobs",jobId.toString());
        Logger.Log(getClass(), "Job:" + jobId + " was sent to DLQ");
    }
    public List<String> getDeadLetterJobs() {
        return jedis.lrange("dead_jobs", 0, -1);
    }
    public UUID blockingDequeue(int time) {
        // Wait forever until a job arrives
        java.util.List<String> result = jedis.brpop(time, "jobs");
        if(result == null) {return null;}
        String id = result.get(1);
        Logger.Log(getClass(), "Returning job:" + id + " from queue");
        return UUID.fromString(id);
    }
}