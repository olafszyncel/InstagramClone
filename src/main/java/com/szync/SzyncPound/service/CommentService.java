package com.szync.SzyncPound.service;

import com.szync.SzyncPound.models.Comment;

import java.util.Optional;

public interface CommentService {
    void saveComment(Comment comment);
    Comment findComment(long commentId);
    void deleteComment(long commentId);
}
