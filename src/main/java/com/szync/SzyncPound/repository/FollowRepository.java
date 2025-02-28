package com.szync.SzyncPound.repository;

import com.szync.SzyncPound.models.Follow;
import com.szync.SzyncPound.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFollowerAndFollowing(UserEntity follower, UserEntity following);
    long countByFollower(UserEntity follower);
    long countByFollowing(UserEntity following);
}
