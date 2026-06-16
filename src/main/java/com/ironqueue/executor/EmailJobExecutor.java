package com.ironqueue.executor;

import java.util.Map;
import com.resend.*;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import io.github.cdimascio.dotenv.Dotenv;
import com.ironqueue.job.Job;
import com.ironqueue.util.Logger;

public class EmailJobExecutor implements JobExecutor {

    @Override
    public void execute(Job job) throws Exception {
        Dotenv dotenv = Dotenv.load();
        Map<String,Object> payload = job.getPayload();
        String reciever = (String)payload.get("to");
        String body =  (String)payload.get("body");
        String subject =  (String)payload.get("subject");

        Logger.Log(getClass(),"Sending emails to " + reciever);
        //Actual work should be done here
        Resend resend = new Resend(dotenv.get("RESEND_API"));

        CreateEmailOptions params = CreateEmailOptions.builder()
                .from(dotenv.get("FROM"))
                .to(reciever)
                .subject(subject)
                .html(body)
                .build();
         try {
            CreateEmailResponse data = resend.emails().send(params);
            System.out.println(data.getId());
        } catch (ResendException e) {
            e.printStackTrace();
        }

        Logger.Log(getClass(),"Email sent to " + reciever);
    }
}