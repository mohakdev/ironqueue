package com.ironqueue.executor;

import java.util.Map;
import com.resend.*;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import com.ironqueue.job.Job;
import com.ironqueue.util.Logger;

public class EmailJobExecutor implements JobExecutor {

    @Override
    public void execute(Job job) throws Exception {
        Map<String,Object> payload = job.getPayload();
        String reciever = (String)payload.get("to");
        String body =  (String)payload.get("body");
        String subject =  (String)payload.get("subject");

        Logger.Log(getClass(),"Sending emails to " + reciever);
        //Actual work should be done here
        String apiKey = System.getenv("RESEND_API_KEY");

        if(apiKey == null || apiKey.isBlank()) {
            Logger.LogError("RESEND_API_KEY not found. \nPlease set it in the environment variables using \nEXPORT RESEND_API_KEY=key");
            throw new IllegalStateException("RESEND_API_KEY environment variable not configured");
        }

        Resend resend = new Resend(apiKey);

        CreateEmailOptions params = CreateEmailOptions.builder()
                .from("mohak@mohakjain.in")
                .to(reciever)
                .subject(subject)
                .html(body)
                .build();
         try {
            CreateEmailResponse data = resend.emails().send(params);
            Logger.LogOutput(data.getId());
        } catch (ResendException e) {
            e.printStackTrace();
        }

        Logger.Log(getClass(),"Email sent to " + reciever);
    }
}