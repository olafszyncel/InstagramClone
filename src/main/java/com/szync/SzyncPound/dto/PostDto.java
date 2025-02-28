package com.szync.SzyncPound.dto;

import com.szync.SzyncPound.mapper.PostMapper;
import com.szync.SzyncPound.models.Comment;
import com.szync.SzyncPound.models.Like;
import com.szync.SzyncPound.models.UserEntity;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    private long id;
    private String content;
    @NotEmpty
    private String photoUrl;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
    private UserEntity user;
    private List<Like> likes;
    private List<Comment> comments;
}
