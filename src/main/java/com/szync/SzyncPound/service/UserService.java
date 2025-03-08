package com.szync.SzyncPound.service;


import com.szync.SzyncPound.dto.RegistrationDto;
import com.szync.SzyncPound.models.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    void saveUser(RegistrationDto registrationDto);

    UserEntity findByEmail(String email);

    UserEntity findByUsername(String username);

    List<String> searchUsers(String query);
    List<Object[]> searchOnlyUsersAndInfluencers(String query);
    List<Object[]> searchUsersInfluencersAndMods(String query);
    Page<String> searchAllMods(Pageable pageable);


    String changeRole(String username, String roleToChange);
}
