package com.ironqueue.worker;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public class WorkerInfo {

    private UUID workerId;
    private Instant lastSeen;

    public WorkerInfo() {
        this.workerId = UUID.randomUUID();
        this.lastSeen = Instant.now();
    }
    public boolean isAlive() {
        return Duration.between(lastSeen,Instant.now()).getSeconds() < 10;
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