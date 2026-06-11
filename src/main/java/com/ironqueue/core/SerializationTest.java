package com.ironqueue.core;

import com.ironqueue.job.Job;
import com.ironqueue.job.JobType;
import com.ironqueue.producer.Producer;

import java.util.HashMap;
import java.util.Map;

public class SerializationTest {

    public static void main(String[] args) {
        try {
            Producer producer = new Producer();

            Map<String, Object> payload = new HashMap<>();
            payload.put("email", "test@example.com");

            Job job1 = new Job(JobType.EMAIL, payload);
            producer.submit(job1);

            payload.clear();
            payload.put("email", "anothertest@gmail.com");

            Job job2 = new Job(JobType.EMAIL, payload);
            producer.submit(job2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}