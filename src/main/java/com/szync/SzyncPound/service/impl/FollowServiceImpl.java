package com.szync.SzyncPound.service.impl;

import com.szync.SzyncPound.models.Follow;
import com.szync.SzyncPound.models.UserEntity;
import com.szync.SzyncPound.repository.FollowRepository;
import com.szync.SzyncPound.repository.UserRepository;
import com.szync.SzyncPound.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;



@Service
public class FollowServiceImpl implements FollowService {

    private FollowRepository followRepository;
    private UserRepository userRepository;

    @Autowired
    public FollowServiceImpl(FollowRepository followRepository, UserRepository userRepository) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void follow(long followerId, long followingId) {
        Follow follow = new Follow();
        UserEntity follower = userRepository.findById(followerId).get();
        UserEntity following = userRepository.findById(followingId).get();
        follow.setFollower(follower);
        follow.setFollowing(following);
        followRepository.save(follow);
    }

    @Override
    public void unfollow(long followId) {
        followRepository.deleteById(followId);
    }

    @Override
    public Optional<Follow> findByFollowerAndFollowing(UserEntity follower, UserEntity following) {
        return followRepository.findByFollowerAndFollowing(follower, following);
    }

    @Override
    public long countFollower(UserEntity user) {
        return followRepository.countByFollower(user);
    }

    @Override
    public long countFollowing(UserEntity user) {
        return followRepository.countByFollowing(user);
    }

    @Override
    public List<UserEntity> getFollowers(UserEntity user) {
        return List.of();
    }

    @Override
    public List<UserEntity> getFollowing(UserEntity user) {
        return List.of();
    }
}
