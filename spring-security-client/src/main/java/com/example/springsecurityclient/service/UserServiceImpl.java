package com.example.springsecurityclient.service;

import com.example.springsecurityclient.entity.PasswordResetToken;
import com.example.springsecurityclient.entity.User;
import com.example.springsecurityclient.entity.VerificationToken;
import com.example.springsecurityclient.model.UserModel;
import com.example.springsecurityclient.repositary.PasswordResetRepositary;
import com.example.springsecurityclient.repositary.UserRepositary;
import com.example.springsecurityclient.repositary.VerificationTokenRepositary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements Userservice{

    @Autowired
    private UserRepositary userRepositary;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private VerificationTokenRepositary verificationTokenRepositary;

    @Autowired
    private PasswordResetRepositary passwordResetRepositary ;

    @Override
    public User registerUser(UserModel userModel) {
        User user = new User();
        user.setFirstName(userModel.getFirstName());
        user.setLastname(userModel.getLastname());
        user.setEmail(userModel.getEmail());
        user.setPassword(passwordEncoder.encode(userModel.getPassword()));
        user.setRole("USER");

        userRepositary.save(user);

        return user;
    }

    @Override
    public void saveVerificationTokenForUser(String token, User user) {

        VerificationToken verificationToken = new VerificationToken(user, token);

        verificationTokenRepositary.save(verificationToken);
    }

    @Override
    public String validateVerificationToken(String token) {
        VerificationToken verificationToken = verificationTokenRepositary.findByToken(token);
        if(verificationToken == null){
            return "invalid";
        }

        User user = verificationToken.getUser();

        Calendar cal = Calendar.getInstance();

        if(verificationToken.getExpirationTime().getTime() - cal.getTime().getTime() <= 0){
            verificationTokenRepositary.delete(verificationToken);
            return "expired";
        }

        user.setEnabled(true);
        userRepositary.save(user);
        return "valid";
    }

    @Override
    public VerificationToken generateNewVerificationToken(String oldToken) {
        VerificationToken verificationToken = verificationTokenRepositary.findByToken(oldToken);
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationTokenRepositary.save(verificationToken);
        return verificationToken;
    }

    @Override
    public User findByEmail(String email) {
        return userRepositary.findByEmail(email);

    }

    @Override
    public void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken passwordResetToken = new PasswordResetToken(user, token);
        passwordResetRepositary.save(passwordResetToken);


    }

    @Override
    public String validatePasswordResetToken(String token) {
        PasswordResetToken passwordResetToken = passwordResetRepositary.findByToken(token);
        if(passwordResetToken == null){
            return "invalid";
        }

        User user = passwordResetToken.getUser();

        Calendar cal = Calendar.getInstance();

        if(passwordResetToken.getExpirationTime().getTime() - cal.getTime().getTime() <= 0){
            passwordResetRepositary.delete(passwordResetToken);
            return "expired";
        }
        return "valid";
    }

    @Override
    public Optional<User> getUserByPasswordResetToken(String token) {
        return Optional.ofNullable(passwordResetRepositary.findByToken(token).getUser());
    }

    @Override
    public void changePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepositary.save(user);

    }

    @Override
    public boolean checkIfOldPassword(User user, String password) {
        return passwordEncoder.matches(password, user.getPassword());
    }
}
