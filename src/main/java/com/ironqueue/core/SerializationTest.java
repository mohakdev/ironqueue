package com.ironqueue.core;

import com.ironqueue.job.Job;
import com.ironqueue.job.JobType;
import com.ironqueue.storage.JsonSerializer;
import com.ironqueue.storage.RedisStorage;
import com.ironqueue.worker.Worker;
import com.ironqueue.producer.Producer;

import java.util.HashMap;
import java.util.Map;

public class SerializationTest {

    public static void main(String[] args) {
        try {
            Producer producer = new Producer();
            Worker worker = new Worker();
            RedisStorage storage = new RedisStorage();

            Map<String, Object> payload = new HashMap<>();
            payload.put("email", "test@example.com");

            Job job = new Job(JobType.EMAIL, payload);
            producer.submit(job);
            worker.processNextJob();
            Job updatedJob = storage.getJob(job.getId());
            System.out.println(updatedJob);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}