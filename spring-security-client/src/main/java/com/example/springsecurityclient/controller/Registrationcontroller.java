package com.example.springsecurityclient.controller;

import com.example.springsecurityclient.entity.User;
import com.example.springsecurityclient.entity.VerificationToken;
import com.example.springsecurityclient.event.RegistrationCompleteEvent;
import com.example.springsecurityclient.model.PasswordModel;
import com.example.springsecurityclient.model.UserModel;
import com.example.springsecurityclient.service.Userservice;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
public class Registrationcontroller {

    @Autowired
    private Userservice userservice;

    @Autowired
    private ApplicationEventPublisher publisher;

    @PostMapping("/register")
    public String registerUser(@RequestBody UserModel userModel, final HttpServletRequest request) {
        User user = userservice.registerUser(userModel);
        publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
        return "Success";
    }


    private String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

    @GetMapping("/verifyRegistration")
    public String verifyRegistration(@RequestParam("token") String token) {
        String result = userservice.validateVerificationToken(token);
        if (result.equalsIgnoreCase("valid")) {
            return "user verifies Successfully";
        }

        return "not verified";

    }

    @GetMapping("/resendVerifyToken")
    public String resendVerificationToken(@RequestParam("token") String oldToken, final HttpServletRequest request) {
        VerificationToken verificationToken = userservice.generateNewVerificationToken(oldToken);
        User user = verificationToken.getUser();

        resendVerificationTokenMail(user, applicationUrl(request), verificationToken);
        return "Verification link sent";


    }

    private void resendVerificationTokenMail(User user, String applicationurl, VerificationToken verificationToken) {
        String url = applicationurl + "/verifyRegistration?token=" + verificationToken.getToken();
        log.info("click here to verify token " + url);


    }


    @PostMapping("/resetpassword")
    public String resetPassword(@RequestBody PasswordModel passwordModel, HttpServletRequest request) {
        User user = userservice.findByEmail(passwordModel.getEmail());
        String url = "";
        if (user != null) {
            String token = UUID.randomUUID().toString();
            userservice.createPasswordResetTokenForUser(user, token);

            url = passwordResetTokenMail(user, applicationUrl(request), token);


        }
        return url;
    }

    private String passwordResetTokenMail(User user, String s, String token) {
        String url = s + "/passwordResetTokenMail?token=" + token;
        log.info("click here to reset password " + url);
        return url;
    }

    @PostMapping("/savePassword")
    public String savePassword(@RequestParam("token") String token, @RequestBody PasswordModel passwordModel) {
        String result = userservice.validatePasswordResetToken(token);
        if (!result.equalsIgnoreCase("valid")) {
            return "invalid token";
        }
        Optional<User> user = userservice.getUserByPasswordResetToken(token);
        if (user.isPresent()) {
            userservice.changePassword(user.get(), passwordModel.getNewPassword());
            return "password reset successfully";
        } else {
            return "invalid token";
        }


    }
    @PostMapping("/changePassword")
    public String changePassword(@RequestBody PasswordModel passwordModel){
        User user = userservice.findByEmail(passwordModel.getEmail());
        if(!userservice.checkIfOldPassword(user, passwordModel.getPassword())){
            return "Invalid old password";
        }

        // save new password
        userservice.changePassword(user, passwordModel.getNewPassword());
        return "password change successfully";

    }

}
