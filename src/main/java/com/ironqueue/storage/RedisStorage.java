package com.ironqueue.storage;

import com.ironqueue.job.Job;
import redis.clients.jedis.UnifiedJedis;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.UUID;

public class RedisStorage {
    private final UnifiedJedis jedis;
    private final JsonSerializer serializer;
    public RedisStorage() {
        this.serializer = new JsonSerializer();
        this.jedis = new UnifiedJedis("redis://localhost:6379");
    }
    public void saveJob(Job job) throws JsonProcessingException
    {
        UUID jobId = job.getId();
        String json = serializer.serialize(job);
        jedis.set("job:"+jobId, json);
    }
    public Job getJob(UUID jobId) throws JsonProcessingException {
        String json = jedis.get("job:" + jobId);
        if (json == null) {return null;}
        return serializer.deserialize(json);
    }
}