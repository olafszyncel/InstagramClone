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

    @Query("SELECT u.username, STRING_AGG(CASE WHEN r.name = 'BANNED' THEN r.name ELSE NULL END, ',')  FROM users u JOIN u.roles r WHERE (LOWER(u.username) LIKE LOWER(CONCAT(:query, '%')) OR LOWER(u.username) LIKE LOWER(CONCAT('%', :query, '%'))) AND NOT EXISTS ( SELECT 1 FROM u.roles r WHERE r.name IN ('ADMIN', 'MOD')) GROUP BY u.username ORDER BY CASE WHEN LOWER(u.username) LIKE LOWER(CONCAT(:query, '%')) THEN 0 ELSE 1 END, u.username LIMIT 10")
    List<Object[]> searchUsernamesAndRoles(String query);

    @Query("SELECT u.username, STRING_AGG(CASE WHEN r.name = 'MOD' THEN r.name ELSE NULL END, ',')  FROM users u JOIN u.roles r WHERE (LOWER(u.username) LIKE LOWER(CONCAT(:query, '%')) OR LOWER(u.username) LIKE LOWER(CONCAT('%', :query, '%'))) AND NOT EXISTS ( SELECT 1 FROM u.roles r WHERE r.name IN ('BANNED')) GROUP BY u.username ORDER BY CASE WHEN LOWER(u.username) LIKE LOWER(CONCAT(:query, '%')) THEN 0 ELSE 1 END, u.username LIMIT 10")
    List<Object[]> searchUsernamesAndRolesWithMods(String query);
}