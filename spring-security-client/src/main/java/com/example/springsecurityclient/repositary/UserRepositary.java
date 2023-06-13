package com.example.springsecurityclient.repositary;

import com.example.springsecurityclient.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepositary extends JpaRepository<User, Long> {

    User findByEmail(String email);
}
