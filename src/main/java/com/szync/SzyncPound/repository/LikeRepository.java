package com.szync.SzyncPound.repository;

import com.szync.SzyncPound.models.Like;
import com.szync.SzyncPound.models.Post;
import com.szync.SzyncPound.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByPostAndUser(Post post, UserEntity user);
}
