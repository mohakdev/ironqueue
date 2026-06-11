package com.ironqueue.worker;

import java.time.Instant;
import java.util.UUID;

public class WorkerInfo {

    private UUID workerId;
    private Instant lastSeen;

    public WorkerInfo() {
        this.workerId = UUID.randomUUID();
        this.lastSeen = Instant.now();
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