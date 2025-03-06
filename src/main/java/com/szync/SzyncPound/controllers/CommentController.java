package com.szync.SzyncPound.controllers;

import com.szync.SzyncPound.dto.CommentDto;
import com.szync.SzyncPound.dto.PostDto;
import com.szync.SzyncPound.models.Comment;
import com.szync.SzyncPound.models.UserEntity;
import com.szync.SzyncPound.security.SecurityUtil;
import com.szync.SzyncPound.service.CommentService;
import com.szync.SzyncPound.service.PostService;
import com.szync.SzyncPound.service.UserService;
import com.szync.SzyncPound.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

import static com.szync.SzyncPound.mapper.PostMapper.mapToPost;

@Controller
public class CommentController {

    private CommentService commentService;
    private PostService postService;
    private UserService userService;
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    public void setCommentService(CommentService commentService, PostService postService, UserService userService, CustomUserDetailsService customUserDetailsService) {
        this.commentService = commentService;
        this.postService = postService;
        this.userService = userService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @PostMapping("/comment/{postId}/new")
    public ResponseEntity<CommentDto> addComment(@PathVariable long postId, @RequestBody CommentDto commentRequest) {
        PostDto post = postService.findPostById(postId);
        if (post == null) {
            return ResponseEntity.badRequest().build();
        }
        String email = SecurityUtil.getSessionUser();
        if(email != null && customUserDetailsService.
                loadUserByUsername(email).getAuthorities().
                stream().noneMatch(a -> a.getAuthority().equals("ROLE_BANNED")))
        {
            UserEntity user = userService.findByEmail(email);

            if(user.getId() != commentRequest.getUserId()) {
                return ResponseEntity.badRequest().build();
            }
            Comment comment = new Comment();
            comment.setContent(commentRequest.getContent());
            comment.setPost(mapToPost(post));
            comment.setUser(user);

            commentService.saveComment(comment);
            long id = comment.getId();
            commentRequest.setId(id);

            return ResponseEntity.ok(commentRequest);
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/comment/{commentId}/delete")
    public ResponseEntity<Map<String, String>> deletePost(@PathVariable("commentId") Long commentId) {
        UserEntity user = userService.findByEmail(SecurityUtil.getSessionUser());
        Comment comment = commentService.findComment(commentId);
        if(comment != null && (user.getId() == comment.getUser().getId() || user.getId() == comment.getPost().getUser().getId())) {
            commentService.deleteComment(commentId);
            return ResponseEntity.ok(Map.of("status", "comment deleted"));
        }
        return ResponseEntity.badRequest().build();
    }


}
