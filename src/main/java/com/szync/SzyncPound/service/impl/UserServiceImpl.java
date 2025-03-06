package com.szync.SzyncPound.service.impl;

import com.szync.SzyncPound.dto.RegistrationDto;
import com.szync.SzyncPound.models.Role;
import com.szync.SzyncPound.models.UserEntity;
import com.szync.SzyncPound.repository.RoleRepository;
import com.szync.SzyncPound.repository.UserRepository;
import com.szync.SzyncPound.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveUser(RegistrationDto registrationDto) {
        UserEntity user = new UserEntity();
        user.setUsername(registrationDto.getUsername());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        Role role = roleRepository.findByName("USER");
        user.setRoles(Arrays.asList(role));
        userRepository.save(user);
    }

    @Override
    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<String> searchUsers(String query) {
        return userRepository.searchUsernames(query);
    }

    @Override
    public List<Object[]> searchOnlyUsersAndInfluencers(String query) {
        return userRepository.searchUsernamesAndRoles(query);
    }

    @Override
    public List<Object[]> searchUsersInfluencersAndMods(String query) {
        return userRepository.searchUsernamesAndRolesWithMods(query);
    }

    @Override
    public String changeRole(String username, String roleToChange) {
        UserEntity user = findByUsername(username);
        Role role = roleRepository.findByName(roleToChange);

        if(user.getRoles().contains(role)) {
            user.getRoles().remove(role);
            userRepository.save(user);
            return "un" + roleToChange;
        }

        user.getRoles().add(role);
        userRepository.save(user);

        return roleToChange;
    }
}