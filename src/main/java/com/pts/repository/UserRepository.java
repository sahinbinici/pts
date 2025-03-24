package com.pts.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pts.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
} 