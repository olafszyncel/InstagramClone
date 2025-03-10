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
public class FollowDto {
    private long id;
    @NotEmpty
    private UserEntity following;
    @NotEmpty
    private UserEntity follower;
    private LocalDateTime createdOn;
}
