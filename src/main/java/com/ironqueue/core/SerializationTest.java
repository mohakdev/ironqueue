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
            for(int i=0; i<10; i++){
                Map<String, Object> payload = new HashMap<>();
                payload.put("email", "test" + i + "@example.com");
                Job job = new Job(JobType.EMAIL, payload);
                producer.submit(job);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}