package com.szync.SzyncPound.service;

import com.szync.SzyncPound.dto.PostDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PostService {
    void createPost(long userId, PostDto postDto);

    Page<PostDto> findAllPosts(Pageable pageable);
    Page<PostDto> findAllByFollowing(long userId, Pageable pageable);

    PostDto findPostById(long postId);

    Page<PostDto> findPostsByUserId(long userId, Pageable pageable);

    void updatePost(PostDto post);

    void deletePost(long postId);

}
