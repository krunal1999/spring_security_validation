package com.example.springsecurityclient.repositary;

import com.example.springsecurityclient.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetRepositary extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String token);
}
