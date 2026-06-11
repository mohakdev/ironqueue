package com.ironqueue.storage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonSerializer {
    private final ObjectMapper mapper;
    public JsonSerializer() {
        this.mapper = new ObjectMapper();
        this.mapper.findAndRegisterModules();
    }
    public String serialize(Object obj) throws JsonProcessingException {
        return mapper.writeValueAsString(obj);
    }
    public <T> T deserialize(String json, Class<T> caller) throws JsonProcessingException {
        return mapper.readValue(json, caller);
    }
}