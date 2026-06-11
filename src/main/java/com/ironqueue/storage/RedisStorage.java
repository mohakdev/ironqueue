package com.ironqueue.storage;

import com.ironqueue.job.Job;
import redis.clients.jedis.UnifiedJedis;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.ironqueue.util.Logger;
import com.ironqueue.worker.WorkerInfo;

import java.util.UUID;

public class RedisStorage {
    private final UnifiedJedis jedis;
    private final JsonSerializer serializer;
    public RedisStorage(UnifiedJedis jedis) {
        this.serializer = new JsonSerializer();
        this.jedis = jedis;
    }
    //Jobs
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
        return serializer.deserialize(json, Job.class);
    }
    //Workers
    public void saveWorker(WorkerInfo workerInfo) throws JsonProcessingException
    {
        String json = serializer.serialize(workerInfo);
        jedis.set("worker:" + workerInfo.getWorkerId(),json);
        jedis.sadd("workers",workerInfo.getWorkerId().toString());
        Logger.Log(getClass(),"Saving worker:" +workerInfo.getWorkerId() +" in Redis");
    }
    public WorkerInfo getWorker(UUID workerId) throws JsonProcessingException
    {
        String json = jedis.get("worker:"+workerId);
        if(json == null) {return null;}
        Logger.Log(getClass(),"Retrieving worker:"+workerId+" from Redis");
        return serializer.deserialize(json,WorkerInfo.class);
    }
}