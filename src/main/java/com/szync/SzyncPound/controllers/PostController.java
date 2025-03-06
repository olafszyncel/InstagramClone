package com.szync.SzyncPound.controllers;

import com.szync.SzyncPound.dto.PostDto;
import com.szync.SzyncPound.models.Post;
import com.szync.SzyncPound.models.UserEntity;
import com.szync.SzyncPound.security.CustomUserDetailsService;
import com.szync.SzyncPound.security.SecurityUtil;
import com.szync.SzyncPound.service.FollowService;
import com.szync.SzyncPound.service.PostService;
import com.szync.SzyncPound.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;

@Controller
public class PostController {
    private CustomUserDetailsService customUserDetailsService;
    private UserService userService;
    private PostService postService;
    private FollowService followService;

    @Autowired
    public PostController(UserService userService, PostService postService, FollowService followService, CustomUserDetailsService customUserDetailsService) {
        this.userService = userService;
        this.postService = postService;
        this.followService = followService;
        this.customUserDetailsService = customUserDetailsService;
    }

    String redirectToPost = "redirect:/post";
    String redirectToMain = "redirect:/";

    @GetMapping("/")
    public String trending(Model model) {
        UserEntity user = new UserEntity();
        String email = SecurityUtil.getSessionUser();
        if(email != null) {
            user = userService.findByEmail(email);
        }

        model.addAttribute("user", user);
        return "posts-list";
    }

    @GetMapping("/following")
    public String following(Model model) {
        String email = SecurityUtil.getSessionUser();
        if(email != null) {
            UserEntity user = userService.findByEmail(email);
            long followersCount = followService.countFollower(user);
            if(followersCount > 0) {
                model.addAttribute("user", user);
                return "posts-list";
            } else {
                return "redirect:/?nofollow";
            }
        }
       return redirectToMain;
    }

    @RequestMapping("/posts")
    @GetMapping
    public ResponseEntity<Page<PostDto>> getPosts(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "5") int size) {
        Page<PostDto> postPage = postService.findAllPosts(PageRequest.of(page, size));
        return ResponseEntity.ok(postPage);
    }

    @RequestMapping("/following/posts")
    @GetMapping
    public ResponseEntity<Page<PostDto>> getPostsFollowing(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "5") int size) {

        String email = SecurityUtil.getSessionUser();
        UserEntity user = userService.findByEmail(email);
        long followersCount = followService.countFollower(user);
        if(followersCount > 0) {
            Page<PostDto> postPage = postService.findAllByFollowing(user.getId(), PageRequest.of(page, size));
            return ResponseEntity.ok(postPage);
        }
        return null;
    }


    @GetMapping("/post/new")
    public String newPost(Model model) {
        String email = SecurityUtil.getSessionUser();
        if(email != null && customUserDetailsService.
                loadUserByUsername(email).getAuthorities().
                stream().noneMatch(a -> a.getAuthority().equals("ROLE_BANNED")))
        {
            model.addAttribute("post", new Post());
            return "post-create";
        }
        return redirectToMain;
    }

    @PostMapping("/post/new")
    public String createPost(@Valid @ModelAttribute("post") PostDto post,BindingResult result, Model model) {
        if(result.hasErrors()) {
            model.addAttribute("post", post);
            return "post-create";
        }
        String email = SecurityUtil.getSessionUser();
        if(email != null && customUserDetailsService.
                loadUserByUsername(email).getAuthorities().
                stream().noneMatch(a -> a.getAuthority().equals("ROLE_BANNED")))
        {
            UserEntity user = userService.findByEmail(email);
            postService.createPost(user.getId(), post);
            return redirectToMain;
        }
        return redirectToMain;
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
        PostDto post = postService.findPostById(postId);
        String email = SecurityUtil.getSessionUser();
        if(email != null && customUserDetailsService.
                loadUserByUsername(email).getAuthorities().
                stream().noneMatch(a -> a.getAuthority().equals("ROLE_BANNED")))
        {
            UserEntity user = userService.findByEmail(email);
            model.addAttribute("user", user);
            model.addAttribute("post", post);
            if(user.getId() == post.getUser().getId()){
                return "post-edit";
            }

        }
        return redirectToPost + postId;
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
            post.setCreatedOn(postDto.getCreatedOn());
            post.setId(postId);
            post.setUser(postDto.getUser());
            postService.updatePost(post);
        }
        return redirectToPost + postId;
    }

    @GetMapping("/post/{postId}/delete")
    public String deletePost(@PathVariable("postId") Long postId) {
        UserEntity user = userService.findByEmail(SecurityUtil.getSessionUser());
        PostDto post = postService.findPostById(postId);
        if(user.getId() == post.getUser().getId()) {
            postService.deletePost(postId);
            return redirectToMain;
        }
        return redirectToPost + postId;
    }
}
