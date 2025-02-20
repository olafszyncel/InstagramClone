package com.szync.SzyncPound.repository;

import com.szync.SzyncPound.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
