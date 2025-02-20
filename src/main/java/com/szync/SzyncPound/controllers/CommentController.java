package com.szync.SzyncPound.controllers;

import com.szync.SzyncPound.dto.CommentDto;
import com.szync.SzyncPound.dto.PostDto;
import com.szync.SzyncPound.models.Comment;
import com.szync.SzyncPound.models.Post;
import com.szync.SzyncPound.models.UserEntity;
import com.szync.SzyncPound.security.SecurityUtil;
import com.szync.SzyncPound.service.CommentService;
import com.szync.SzyncPound.service.PostService;
import com.szync.SzyncPound.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ResourceBundle;

import static com.szync.SzyncPound.mapper.PostMapper.mapToPost;

@Controller
public class CommentController {

    private CommentService commentService;
    private PostService postService;
    private UserService userService;

    @Autowired
    public void setCommentService(CommentService commentService, PostService postService, UserService userService) {
        this.commentService = commentService;
        this.postService = postService;
        this.userService = userService;
    }

    @PostMapping("/comment/{postId}/new")
    public ResponseEntity<CommentDto> addComment(@PathVariable long postId, @RequestBody CommentDto commentRequest) {
        PostDto post = postService.findPostById(postId);
        if (post == null) {
            return ResponseEntity.badRequest().build();
        }
        UserEntity user = new UserEntity();
        String email = SecurityUtil.getSessionUser();
        if(email != null) {
            user = userService.findByEmail(email);
            if(user.getId() != commentRequest.getUserId()) {
                return ResponseEntity.badRequest().build();
            }
            Comment comment = new Comment();
            comment.setContent(commentRequest.getContent());
            comment.setPost(mapToPost(post));
            comment.setUser(user);

            commentService.saveComment(comment);

            return ResponseEntity.ok(commentRequest);
        }
        return ResponseEntity.badRequest().build();
    }


}
