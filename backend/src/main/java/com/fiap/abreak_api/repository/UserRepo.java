package com.fiap.abreak_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fiap.abreak_api.model.User;


public interface UserRepo extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
    
}
