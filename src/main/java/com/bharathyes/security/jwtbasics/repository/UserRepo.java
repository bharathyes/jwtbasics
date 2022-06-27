package com.bharathyes.security.jwtbasics.repository;

import com.bharathyes.security.jwtbasics.entity.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<UserDetail, Long> {
    Optional<UserDetail> findByEmail(String email);
}
