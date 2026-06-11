package com.ironqueue.executor;

import com.ironqueue.job.Job;
import com.ironqueue.util.Logger;

public class EmailJobExecutor implements JobExecutor {

    @Override
    public void execute(Job job) throws Exception {
        String email = (String)job.getPayload().get("email");

        // System.out.println(
        //     "[EMAIL] Sending email to " + email
        // );
        Logger.Log(getClass(),"Sending emails to " + email);
        //Actual work should be done here
        Thread.sleep(2000);
        Logger.Log(getClass(),"Email sent to " + email);

        // System.out.println(
        //     "[EMAIL] Email sent to " + email
        // );
    }
}