package com.szync.SzyncPound.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class RegistrationDto {
    private Long id;
    @NotEmpty
    private String username;
    @NotEmpty
    private String fullName;
    @NotEmpty
    @Email
    private String email;
    @NotEmpty
    private String password;
}
