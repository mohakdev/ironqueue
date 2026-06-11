package com.ironqueue.storage;

import com.ironqueue.job.Job;
import redis.clients.jedis.UnifiedJedis;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.ironqueue.util.Logger;
import java.util.UUID;

public class RedisStorage {
    private final UnifiedJedis jedis;
    private final JsonSerializer serializer;
    public RedisStorage(UnifiedJedis jedis) {
        this.serializer = new JsonSerializer();
        this.jedis = jedis;
    }
    public void saveJob(Job job) throws JsonProcessingException
    {
        UUID jobId = job.getId();
        String json = serializer.serialize(job);
        jedis.set("job:"+jobId, json);
        Logger.Log(getClass(), "Saving job:"+jobId+" in Redis");

    }
    public Job getJob(UUID jobId) throws JsonProcessingException {
        String json = jedis.get("job:" + jobId);
        if (json == null) {return null;}
        Logger.Log(getClass(), "Retrieving job:"+jobId+" from Redis");
        return serializer.deserialize(json);
    }
}