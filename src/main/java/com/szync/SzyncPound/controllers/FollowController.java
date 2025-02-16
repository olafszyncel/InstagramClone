package com.szync.SzyncPound.controllers;


import com.szync.SzyncPound.dto.FollowDto;
import com.szync.SzyncPound.dto.PostDto;
import com.szync.SzyncPound.models.Follow;
import com.szync.SzyncPound.models.Post;
import com.szync.SzyncPound.models.UserEntity;
import com.szync.SzyncPound.security.SecurityUtil;
import com.szync.SzyncPound.service.FollowService;
import com.szync.SzyncPound.service.PostService;
import com.szync.SzyncPound.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
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
        UserEntity user = new UserEntity();
        String email = SecurityUtil.getSessionUser();
        if(email != null) {
            user = userService.findByEmail(email);
            return "redirect:/profile/" + user.getUsername();
        }
        return "redirect:/";
    }

    @GetMapping("/profile/{username}")
    public String profile(@PathVariable("username") String username, Model model) {
        UserEntity userProfile = userService.findByUsername(username);
        UserEntity user = new UserEntity();
        String email = SecurityUtil.getSessionUser();
        if(email != null) {
            user = userService.findByEmail(email);
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
    public String follow(@PathVariable("profileUsername") String profileUsername, Model model) {
        UserEntity user = new UserEntity();

        String email = SecurityUtil.getSessionUser();
        if(email != null) {
            user = userService.findByEmail(email);
            UserEntity profileUser = userService.findByUsername(profileUsername);
            if(user != profileUser) {
                // sprawdz czy jest juz obs jesli tak to usun
                Optional<Follow> existingFollow = followService.findByFollowerAndFollowing(user, profileUser);
                if(existingFollow.isPresent()) {
                    //tu delete
                    followService.unfollow(existingFollow.get().getId());
                }
                else {
                    followService.follow(user.getId(), profileUser.getId());
                }
            }
        }

        return "redirect:/profile/" + profileUsername;
    }

}
