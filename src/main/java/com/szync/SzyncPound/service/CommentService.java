package com.szync.SzyncPound.service;

import com.szync.SzyncPound.models.Comment;

public interface CommentService {
    void saveComment(Comment comment);
    Comment findComment(long commentId);
    void deleteComment(long commentId);
}
