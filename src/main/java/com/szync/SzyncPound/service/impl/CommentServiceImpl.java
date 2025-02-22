package com.szync.SzyncPound.service.impl;

import com.szync.SzyncPound.models.Comment;
import com.szync.SzyncPound.repository.CommentRepository;
import com.szync.SzyncPound.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {
    private CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public void saveComment(Comment comment) {
        commentRepository.save(comment);
    }

    @Override
    public Comment findComment(long commentId) {
        return commentRepository.findById(commentId).get();
    }

    @Override
    public void deleteComment(long commentId) {
        commentRepository.deleteById(commentId);
    }


}
