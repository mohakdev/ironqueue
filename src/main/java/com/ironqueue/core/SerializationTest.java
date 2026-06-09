package com.ironqueue.core;

import com.ironqueue.job.Job;
import com.ironqueue.job.JobType;
import com.ironqueue.storage.JsonSerializer;
import com.ironqueue.storage.RedisStorage;

import java.util.HashMap;
import java.util.Map;

public class SerializationTest {

    public static void main(String[] args) {
        try {
            // Create payload
            Map<String, Object> payload = new HashMap<>();
            payload.put("email", "test@example.com");

            // Create job
            Job job = new Job(JobType.EMAIL, payload);
            RedisStorage storage = new RedisStorage();
            // Save job
            storage.saveJob(job);
            System.out.println("\n===== SAVED JOBS =====");
            System.out.println(storage.getJob(job.getId()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}