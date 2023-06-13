package com.example.springsecurityclient.event.listener;

import com.example.springsecurityclient.entity.User;
import com.example.springsecurityclient.event.RegistrationCompleteEvent;
import com.example.springsecurityclient.service.Userservice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class RegistrationCompleteEventListener implements ApplicationListener <RegistrationCompleteEvent> {

    @Autowired
    private Userservice userservice;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {

        // crete the Verification token for the user with link
        User user = event.getUser();

        String token = UUID.randomUUID().toString();

        // after getting token and user save to database
        userservice.saveVerificationTokenForUser(token, user);

        // send mail to user
        String url = event.getApplicationUrl() + "/verifyRegistration?token=" + token;

        // sendVerificationMail() . we need to create it
        log.info("click the link to verify your account:{}", url);
    }
}
