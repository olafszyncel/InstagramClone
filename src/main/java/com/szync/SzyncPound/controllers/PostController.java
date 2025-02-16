package com.szync.SzyncPound.controllers;

import com.szync.SzyncPound.dto.PostDto;
import com.szync.SzyncPound.models.Post;
import com.szync.SzyncPound.models.UserEntity;
import com.szync.SzyncPound.security.SecurityUtil;
import com.szync.SzyncPound.service.FollowService;
import com.szync.SzyncPound.service.PostService;
import com.szync.SzyncPound.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.validation.Valid;

import java.util.List;

@Controller
public class PostController {
    private UserService userService;
    private PostService postService;
    private FollowService followService;

    @Autowired
    public PostController(UserService userService, PostService postService, FollowService followService) {
        this.userService = userService;
        this.postService = postService;
        this.followService = followService;
    }

    @GetMapping("/")
    public String trending(Model model) {
        UserEntity user = new UserEntity();
        List<PostDto> posts = postService.findAllPosts();
        String email = SecurityUtil.getSessionUser();
        if(email != null) {
            user = userService.findByEmail(email);
        }
        model.addAttribute("user", user);
        model.addAttribute("posts", posts);
        return "posts-list";
    }

    @GetMapping("/following")
    public String following(Model model) {
        UserEntity user = new UserEntity();
        String email = SecurityUtil.getSessionUser();

        if(email != null) {
            user = userService.findByEmail(email);
            long followersCount = followService.countFollower(user);
            if(followersCount > 0) {
                List<PostDto> posts = postService.findAllByFollowing(user.getId());
                model.addAttribute("user", user);
                model.addAttribute("posts", posts);
                return "posts-list";
            } else {
                return "redirect:/?nofollow";
            }

        }
       return "redirect:/";
    }

    @GetMapping("/post/new")
    public String newPost(Model model) {
        UserEntity user = new UserEntity();
        String email = SecurityUtil.getSessionUser();
        if(email != null) {
            model.addAttribute("post", new Post());
            return "post-create";
        }
        return "redirect:/";
    }

    @PostMapping("/post/new")
    public String createPost(@Valid @ModelAttribute("post") PostDto post,BindingResult result, Model model) {
        if(result.hasErrors()) {
            model.addAttribute("post", post);
            return "post-create";
        }
        UserEntity user = new UserEntity();
        String email = SecurityUtil.getSessionUser();
        if(email != null) {
            user = userService.findByEmail(email);
            postService.createPost(user.getId(), post);
            return "redirect:/";
        }
        return "redirect:/";
    }

    @GetMapping("/post/{postId}")
    public String postDetails(@PathVariable("postId") Long postId, Model model) {
        UserEntity user = new UserEntity();
        PostDto post = postService.findPostById(postId);
        String email = SecurityUtil.getSessionUser();
        if(email != null) {
            user = userService.findByEmail(email);
        }
        model.addAttribute("user", user);
        model.addAttribute("post", post);
        return "post-details";
    }

    @GetMapping("/post/{postId}/edit")
    public String editPost(@PathVariable("postId") Long postId, Model model) {
        UserEntity user = new UserEntity();
        PostDto post = postService.findPostById(postId);
        String email = SecurityUtil.getSessionUser();
        if(email != null) {
            user = userService.findByEmail(email);
            model.addAttribute("user", user);
            model.addAttribute("post", post);
            if(user.getId() == post.getUser().getId()){
                return "post-edit";
            }

        }
        return "redirect:/post/" + postId;
    }

    @PostMapping("/post/{postId}/edit")
    public String updatePost(@PathVariable("postId") long postId,
                             @Valid @ModelAttribute("post") PostDto post,
                             BindingResult result, Model model) {
        if(result.hasErrors()) {
            model.addAttribute("post", post);
            return "post-edit";
        }
        UserEntity user = userService.findByEmail(SecurityUtil.getSessionUser());
        PostDto postDto = postService.findPostById(postId);
        if(user.getId() == postDto.getUser().getId()) {
            post.setId(postId);
            post.setUser(postDto.getUser());
            postService.updatePost(post);
        }
        return "redirect:/post/" + postId;
    }

    @GetMapping("/post/{postId}/delete")
    public String deletePost(@PathVariable("postId") Long postId) {
        UserEntity user = userService.findByEmail(SecurityUtil.getSessionUser());
        PostDto post = postService.findPostById(postId);
        if(user.getId() == post.getUser().getId()) {
            postService.deletePost(postId);
            return "redirect:/";
        }
        return "redirect:/post/" + postId;
    }
}
