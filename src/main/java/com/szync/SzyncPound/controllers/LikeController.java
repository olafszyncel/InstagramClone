package com.szync.SzyncPound.controllers;

import com.szync.SzyncPound.models.UserEntity;
import com.szync.SzyncPound.security.SecurityUtil;
import com.szync.SzyncPound.service.LikeService;
import com.szync.SzyncPound.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import com.szync.SzyncPound.models.Like;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;
import java.util.Optional;

@Controller
public class LikeController {
    private LikeService likeService;
    private UserService userService;

    @Autowired
    public void setLikeController(LikeService likeService, UserService userService) {
        this.likeService = likeService;
        this.userService = userService;
    }

    String statusConst = "status";
    String errorConst = "error";

    @PostMapping("/like/{postId}")
    public ResponseEntity<Map<String, String>> likePost(@PathVariable long postId) {

        String email = SecurityUtil.getSessionUser();
        if(email != null) {
            UserEntity user = userService.findByEmail(email);

            Optional<Like> existingLike = likeService.findByPostAndUser(postId, user.getId());
            if(!existingLike.isPresent()) {
                likeService.createLike(postId, user.getId());
                return ResponseEntity.ok(Map.of(statusConst, "liked"));
            } else {
                return ResponseEntity.ok(Map.of(statusConst, "already_liked"));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(errorConst, "User not logged in"));
    }

    @PostMapping("/unlike/{postId}")
    public ResponseEntity<Map<String, String>> unlikePost(@PathVariable long postId) {
        String email = SecurityUtil.getSessionUser();
        if(email != null) {
            UserEntity user = userService.findByEmail(email);

            Optional<Like> existingLike = likeService.findByPostAndUser(postId, user.getId());

            if(existingLike.isPresent()) {
                likeService.deleteLike(existingLike.get().getId());

                return ResponseEntity.ok(Map.of(statusConst, "unliked"));

            } else {
                return ResponseEntity.ok(Map.of(statusConst, "already_unliked"));
            }

        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(errorConst, "User not logged in"));
    }
}
