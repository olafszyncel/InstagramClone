package com.szync.SzyncPound.mapper;

import com.szync.SzyncPound.dto.PostDto;
import com.szync.SzyncPound.models.Post;

public class PostMapper {
    public static Post mapToPost(PostDto postDto) {
        return Post.builder()
                .id(postDto.getId())
                .content(postDto.getContent())
                .photoUrl(postDto.getPhotoUrl())
                .createdOn(postDto.getCreatedOn())
                .updatedOn(postDto.getUpdatedOn())
                .user(postDto.getUser())
                .likes(postDto.getLikes())
                .comments(postDto.getComments().reversed())
                .build();
    }

    public static PostDto mapToPostDto(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .content(post.getContent())
                .photoUrl(post.getPhotoUrl())
                .createdOn(post.getCreatedOn())
                .updatedOn(post.getUpdatedOn())
                .user(post.getUser())
                .likes(post.getLikes())
                .comments(post.getComments().reversed())
                .build();
    }
}
