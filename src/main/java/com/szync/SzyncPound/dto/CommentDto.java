package com.szync.SzyncPound.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private long id;
    @NotEmpty
    private String content;
    @NotEmpty
    private long userId;
}
