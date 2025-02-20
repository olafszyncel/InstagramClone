package com.szync.SzyncPound.service;

import com.szync.SzyncPound.models.Follow;
import com.szync.SzyncPound.models.Like;


import java.util.Optional;

public interface LikeService {
    void createLike(long postId, long userId);
    void deleteLike(long likeId);

    Optional<Like> findByPostAndUser(long postId, long userId);

}
