package com.szync.SzyncPound.service;


import com.szync.SzyncPound.dto.RegistrationDto;
import com.szync.SzyncPound.models.UserEntity;

import java.util.List;

public interface UserService {
    void saveUser(RegistrationDto registrationDto);

    UserEntity findByEmail(String email);

    UserEntity findByUsername(String username);

    List<String> searchUsers(String query);
}
