package com.szync.SzyncPound.service.impl;

import com.szync.SzyncPound.models.Like;
import com.szync.SzyncPound.models.Post;
import com.szync.SzyncPound.models.UserEntity;
import com.szync.SzyncPound.repository.LikeRepository;
import com.szync.SzyncPound.repository.PostRepository;
import com.szync.SzyncPound.repository.UserRepository;
import com.szync.SzyncPound.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LikeServiceImpl implements LikeService {
    private final PostRepository postRepository;
    private UserRepository userRepository;
    private LikeRepository likeRepository;

    @Autowired
    public LikeServiceImpl(LikeRepository likeRepository, UserRepository userRepository, PostRepository postRepository) {
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }


    @Override
    public void createLike(long postId, long userId) {
        Like like = new Like();
        UserEntity user = userRepository.findById(userId).get();
        Post post = postRepository.findById(postId).get();
        like.setUser(user);
        like.setPost(post);
        likeRepository.save(like);
    }

    @Override
    public void deleteLike(long likeId) {
        likeRepository.deleteById(likeId);
    }

    @Override
    public Optional<Like> findByPostAndUser(long postId, long userId) {
        Post post = postRepository.findById(postId).get();
        UserEntity user = userRepository.findById(userId).get();
        return likeRepository.findByPostAndUser(post, user);
    }
}
