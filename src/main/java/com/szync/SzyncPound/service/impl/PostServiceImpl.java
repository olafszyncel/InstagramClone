package com.szync.SzyncPound.service.impl;

import com.szync.SzyncPound.dto.PostDto;
import com.szync.SzyncPound.models.Post;
import com.szync.SzyncPound.models.UserEntity;
import com.szync.SzyncPound.repository.PostRepository;
import com.szync.SzyncPound.repository.UserRepository;
import com.szync.SzyncPound.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<PostDto> findAllPosts() {
        List<Post> posts = postRepository.findAllByOrderByIdDesc();
        return posts.stream().map(post -> mapToPostDto(post)).collect(Collectors.toList());
    }

    @Override
    public PostDto findPostById(long postId) {
        Post post = postRepository.findById(postId).get();
        return mapToPostDto(post);
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
