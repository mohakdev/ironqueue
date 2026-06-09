package com.ironqueue.storage;

import com.ironqueue.job.Job;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonSerializer {
    private final ObjectMapper mapper;
    public JsonSerializer() {
        this.mapper = new ObjectMapper();
        this.mapper.findAndRegisterModules();
    }
    public String serialize(Job job) throws JsonProcessingException {
        return mapper.writeValueAsString(job);
    }
    public Job deserialize(String json) throws JsonProcessingException {
        return mapper.readValue(json, Job.class);
    }
}