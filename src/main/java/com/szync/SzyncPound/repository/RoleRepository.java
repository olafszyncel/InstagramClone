package com.szync.SzyncPound.repository;

import com.szync.SzyncPound.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}

