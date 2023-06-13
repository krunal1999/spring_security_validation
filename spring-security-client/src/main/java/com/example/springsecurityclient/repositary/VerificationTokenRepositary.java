package com.example.springsecurityclient.repositary;

import com.example.springsecurityclient.entity.User;
import com.example.springsecurityclient.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepositary extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);
}
