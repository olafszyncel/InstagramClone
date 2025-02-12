package com.szync.SzyncPound.dto;

import com.szync.SzyncPound.models.UserEntity;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    private long id;
    @NotEmpty
    private String content;
    @NotEmpty
    private String photoUrl;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
    private UserEntity user;
}
