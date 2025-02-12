package com.szync.SzyncPound.service;

import com.szync.SzyncPound.dto.PostDto;
import com.szync.SzyncPound.models.Post;

import java.util.List;

public interface PostService {
    void createPost(long userId, PostDto postDto);

    List<PostDto> findAllPosts();

    PostDto findPostById(long postId);

    void updatePost(PostDto post);

    void deletePost(long postId);
}
