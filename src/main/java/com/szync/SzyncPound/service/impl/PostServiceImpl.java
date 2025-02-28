package com.szync.SzyncPound.service.impl;

import com.szync.SzyncPound.dto.PostDto;
import com.szync.SzyncPound.models.Post;
import com.szync.SzyncPound.models.UserEntity;
import com.szync.SzyncPound.repository.PostRepository;
import com.szync.SzyncPound.repository.UserRepository;
import com.szync.SzyncPound.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.szync.SzyncPound.mapper.PostMapper.mapToPost;
import static com.szync.SzyncPound.mapper.PostMapper.mapToPostDto;

@Service
public class PostServiceImpl implements PostService {
    private PostRepository postRepository;
    private UserRepository userRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void createPost(long userId, PostDto postDto) {
        UserEntity user = userRepository.findById(userId).get();
        Post post = mapToPost(postDto);
        post.setUser(user);
        postRepository.save(post);
    }

    @Override
    public Page<PostDto> findAllPosts(Pageable pageable) {
        List<Post> posts = postRepository.findAllByOrderByIdDesc(pageable);
        List<PostDto> postsDto = posts.stream().map(post -> mapToPostDto(post)).collect(Collectors.toList());
        return new PageImpl<>(postsDto);
    }

    @Override
    public Page<PostDto> findAllByFollowing(long userId, Pageable pageable) {
        List<Post> posts = postRepository.findPostsByFollowing(userId, pageable);
        List<PostDto> postsDto = posts.stream().map(post -> mapToPostDto(post)).collect(Collectors.toList());
        return new PageImpl<>(postsDto);
    }

    @Override
    public PostDto findPostById(long postId) {
        Post post = postRepository.findById(postId).get();
        return mapToPostDto(post);
    }

    @Override
    public Page<PostDto> findPostsByUserId(long userId, Pageable pageable) {
        List<Post> posts = postRepository.findAllByUserIdOrderByIdDesc(userId, pageable);
        List<PostDto> postsDto = posts.stream().map(post -> mapToPostDto(post)).collect(Collectors.toList());
        return new PageImpl<>(postsDto);
    }

    @Override
    public void updatePost(PostDto postDto) {
        Post post = mapToPost(postDto);
        postRepository.save(post);
    }

    @Override
    public void deletePost(long postId) {
        postRepository.deleteById(postId);
    }
}
