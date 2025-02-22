package com.szync.SzyncPound.controllers;

import com.szync.SzyncPound.dto.PostDto;
import com.szync.SzyncPound.models.UserEntity;
import com.szync.SzyncPound.security.SecurityUtil;
import com.szync.SzyncPound.service.LikeService;
import com.szync.SzyncPound.service.PostService;
import com.szync.SzyncPound.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import com.szync.SzyncPound.models.Like;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class LikeController {
    private LikeService likeService;
    private PostService postService;
    private UserService userService;

    @Autowired
    public void LikeController(LikeService likeService, PostService postService, UserService userService) {
        this.likeService = likeService;
        this.postService = postService;
        this.userService = userService;
    }

    @PostMapping("/like/{postId}")
    public ResponseEntity<?> likePost(@PathVariable long postId) {
        UserEntity user = new UserEntity();
        String email = SecurityUtil.getSessionUser();
        if(email != null) {
            user = userService.findByEmail(email);

            Optional<Like> existingLike = likeService.findByPostAndUser(postId, user.getId());
            if(!existingLike.isPresent()) {
                likeService.createLike(postId, user.getId());
                return ResponseEntity.ok(Map.of("status", "liked"));
            } else {
                return ResponseEntity.ok(Map.of("status", "already_liked"));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "User not logged in"));
    }

    @PostMapping("/unlike/{postId}")
    public ResponseEntity<?> unlikePost(@PathVariable long postId) {
        UserEntity user = new UserEntity();
        String email = SecurityUtil.getSessionUser();
        if(email != null) {
            user = userService.findByEmail(email);

            Optional<Like> existingLike = likeService.findByPostAndUser(postId, user.getId());

            if(existingLike.isPresent()) {
                likeService.deleteLike(existingLike.get().getId());

                return ResponseEntity.ok(Map.of("status", "unliked"));

            } else {
                return ResponseEntity.ok(Map.of("status", "already_unliked"));
            }

        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "User not logged in"));
    }
}
