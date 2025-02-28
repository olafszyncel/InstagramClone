package com.szync.SzyncPound.service;

import com.szync.SzyncPound.models.Follow;
import com.szync.SzyncPound.models.UserEntity;

import java.util.List;
import java.util.Optional;

public interface FollowService {
    void follow(long followerId, long followingId);
    void unfollow(long followId);

    Optional<Follow> findByFollowerAndFollowing(UserEntity follower, UserEntity following);

    long countFollower(UserEntity user);
    long countFollowing(UserEntity user);

    List<UserEntity> getFollowers(UserEntity user);
    List<UserEntity> getFollowing(UserEntity user);
}
