package com.ironqueue.executor;

import com.ironqueue.job.JobType;

public class JobExecutorFactory {

    public static JobExecutor getExecutor(JobType type) {

        switch(type) {
            case EMAIL:
                return new EmailJobExecutor();

            default:
                throw new IllegalArgumentException(
                    "Unknown job type: " + type
                );
        }
    }
}