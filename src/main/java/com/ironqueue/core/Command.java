package com.ironqueue.core;

public abstract class Command {
    private final String description;

    public Command(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }

    public abstract void execute(String[] args) throws Exception;
}
