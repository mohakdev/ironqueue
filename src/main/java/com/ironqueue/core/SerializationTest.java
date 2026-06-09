package com.ironqueue.core;

import com.ironqueue.job.Job;
import com.ironqueue.job.JobType;
import com.ironqueue.storage.JsonSerializer;

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

            // Create serializer
            JsonSerializer serializer = new JsonSerializer();

            // Serialize
            String json = serializer.serialize(job);

            System.out.println("===== ORIGINAL JOB =====");
            System.out.println(job);

            System.out.println("\n===== JSON =====");
            System.out.println(json);

            // Deserialize
            Job restoredJob = serializer.deserialize(json);

            System.out.println("\n===== RESTORED JOB =====");
            System.out.println(restoredJob);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}