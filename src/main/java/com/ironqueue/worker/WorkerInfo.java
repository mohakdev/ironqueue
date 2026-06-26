package com.ironqueue.worker;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class WorkerInfo {

    private UUID workerId;
    private Instant lastSeen;
    private int jobsProcessed;

    public WorkerInfo() {
        this.workerId = UUID.randomUUID();
        this.lastSeen = Instant.now();
        this.jobsProcessed = 0;
    }
    @JsonIgnore
    public boolean isAlive() {
        return Duration.between(lastSeen,Instant.now()).getSeconds() < 10;
    }

    public void incrementJobProcessed() {
        jobsProcessed++;
    }
    public int getJobsProcessed() {
        return jobsProcessed;
    }

    public UUID getWorkerId() {
        return workerId;
    }

    public Instant getLastSeen() {
        return lastSeen;
    }

    public void heartbeat() {
        this.lastSeen = Instant.now();
    }
}