package com.ironqueue.executor;

import com.ironqueue.job.Job;

public interface JobExecutor {
    void execute(Job job) throws Exception;
}