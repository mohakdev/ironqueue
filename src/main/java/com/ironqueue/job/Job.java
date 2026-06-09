package com.ironqueue.job;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public class Job {

    private UUID id;
    private JobType type;
    private JobStatus status;
    private Map<String, Object> payload;
    private Instant createdAt;
    private UUID assignedWorkerId;

    public Job(JobType type, Map<String, Object> payload) {
        this.id = UUID.randomUUID();
        this.type = type;
        this.payload = payload;
        this.status = JobStatus.PENDING;
        this.createdAt = Instant.now();
        this.assignedWorkerId = null;
    }

    // Required by Jackson for deserialization
    public Job() {}

    //GETTERS AND SETTERS
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id;}

    public JobType getType() {return type;}
    public void setType(JobType type) {this.type = type;}

    public JobStatus getStatus() {return status;}
    public void setStatus(JobStatus status) {this.status = status;}

    public Map<String, Object> getPayload() {return payload;}
    public void setPayload(Map<String, Object> payload) {this.payload = payload;}

    public Instant getCreatedAt() {return createdAt;}
    public void setCreatedAt(Instant createdAt) {this.createdAt = createdAt;}
    
    public UUID getAssignedWorkerId() {return assignedWorkerId;}
    public void setAssignedWorkerId(UUID assignedWorkerId) {this.assignedWorkerId = assignedWorkerId;}

    @Override
    public String toString() {
        return "Job{" +
                "id=" + id +
                ", type=" + type +
                ", status=" + status +
                ", payload=" + payload +
                ", createdAt=" + createdAt +
                ", assignedWorkerId=" + assignedWorkerId +
                '}';
    }
}