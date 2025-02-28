package com.szync.SzyncPound.repository;

import com.szync.SzyncPound.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
    UserEntity findByUsername(String username);
    UserEntity findFirstByEmail(String email);

    @Query("SELECT username FROM users WHERE username LIKE CONCAT(:query, '%') OR username LIKE CONCAT('%', :query, '%') ORDER BY CASE WHEN username LIKE CONCAT(:query, '%') THEN 0 ELSE 1 END, username LIMIT 10")
    List<String> searchUsernames(String query);
}