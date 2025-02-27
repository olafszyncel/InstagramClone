package com.szync.SzyncPound.repository;

import com.szync.SzyncPound.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByIdDesc();
    List<Post> findAllByUserIdOrderByIdDesc(long userId);

    @Query("SELECT p FROM posts p WHERE p.user.id IN (SELECT f.following.id FROM Follow f WHERE f.follower.id = :userId) ORDER BY p.id DESC")
    List<Post> findPostsByFollowing(@Param("userId") long userId);

}
