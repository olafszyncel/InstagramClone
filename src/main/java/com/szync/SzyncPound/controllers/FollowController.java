package com.szync.SzyncPound.controllers;

import com.szync.SzyncPound.dto.PostDto;
import com.szync.SzyncPound.models.Follow;
import com.szync.SzyncPound.models.UserEntity;
import com.szync.SzyncPound.security.SecurityUtil;
import com.szync.SzyncPound.service.FollowService;
import com.szync.SzyncPound.service.PostService;
import com.szync.SzyncPound.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class FollowController {
    private UserService userService;
    private PostService postService;
    private FollowService followService;

    @Autowired
    public FollowController(UserService userService, PostService postService, FollowService followService) {
        this.userService = userService;
        this.postService = postService;
        this.followService = followService;
    }

    @GetMapping("/profile")
    public String profile() {
        String email = SecurityUtil.getSessionUser();
        if(email != null) {
            UserEntity user = userService.findByEmail(email);
            return "redirect:/profile/" + user.getUsername();
        }
        return "redirect:/";
    }

    @GetMapping("/profile/{username}")
    public String profile(@PathVariable("username") String username, Model model) {
        UserEntity userProfile = userService.findByUsername(username);
        String email = SecurityUtil.getSessionUser();
        if(email != null) {
            UserEntity user = userService.findByEmail(email);
            model.addAttribute("user", user);
            Optional<Follow> existingFollow = followService.findByFollowerAndFollowing(user, userProfile);
            model.addAttribute("follow", existingFollow.isPresent());
        }
        long followersCount = followService.countFollower(userProfile);
        long followingsCount = followService.countFollowing(userProfile);
        List<PostDto> posts = postService.findPostsByUserId(userProfile.getId());
        model.addAttribute("followersCount", followersCount);
        model.addAttribute("followingsCount", followingsCount);
        model.addAttribute("userProfile", userProfile);
        model.addAttribute("posts", posts);
        return "profile";
    }

    @PostMapping("/follow/{profileUsername}")
    public ResponseEntity<Map<String, String>> follow(@PathVariable("profileUsername") String profileUsername) {
        String email = SecurityUtil.getSessionUser();
        if(email != null) {
            UserEntity user = userService.findByEmail(email);
            UserEntity profileUser = userService.findByUsername(profileUsername);
            if(user != profileUser) {
                Optional<Follow> existingFollow = followService.findByFollowerAndFollowing(user, profileUser);
                if(existingFollow.isPresent()) {
                    followService.unfollow(existingFollow.get().getId());
                    return ResponseEntity.ok(Map.of("status", "unfollowed"));
                }
                else {
                    followService.follow(user.getId(), profileUser.getId());
                    return ResponseEntity.ok(Map.of("status", "followed"));
                }
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "User not logged in"));
    }

    @GetMapping("/search/profile")
    public ResponseEntity<List<String>> searchProfile(@RequestParam(value="query") String query) {
        List<String> usernames = userService.searchUsers(query);
        return ResponseEntity.ok(usernames);
    }

}
