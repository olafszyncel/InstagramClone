package com.szync.SzyncPound.controllers;

import com.szync.SzyncPound.models.UserEntity;
import com.szync.SzyncPound.security.SecurityUtil;
import com.szync.SzyncPound.service.FollowService;
import com.szync.SzyncPound.service.PostService;
import com.szync.SzyncPound.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.springframework.security.authorization.AuthorityAuthorizationManager.hasAnyRole;

@Controller
public class ManagementController {

    private UserService userService;
    private PostService postService;
    private FollowService followService;

    @Autowired
    public ManagementController(UserService userService, PostService postService, FollowService followService) {
        this.userService = userService;
        this.postService = postService;
        this.followService = followService;
    }

    @GetMapping("/management")
    public String userManagement(Model model) {
        UserEntity user = new UserEntity();
        String email = SecurityUtil.getSessionUser();
        if(email != null) {
            user = userService.findByEmail(email);
        }
        model.addAttribute("user", user);
        return "management";
    }

    @GetMapping("/management/search/ban")
    public ResponseEntity<List<Object[]>> searchProfile(@RequestParam(value="query") String query) {
        List<Object[]> usernames = userService.searchOnlyUsersAndInfluencers(query);
        return ResponseEntity.ok(usernames);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MOD')")
    @PostMapping("/management/{username}/ban")
    public ResponseEntity<Map<String, String>> banUser(Model model, @PathVariable String username) {

        String response = userService.changeRole(username, "BANNED");
        return ResponseEntity.ok(Map.of("roleStatus", response));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MOD')")
    @GetMapping("/management/mod")
    public String modManagement(Model model) {
        UserEntity user = new UserEntity();
        String email = SecurityUtil.getSessionUser();
        if(email != null) {
            user = userService.findByEmail(email);
        }
        model.addAttribute("user", user);
        return "management-mod";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/management/search/mod")
    public ResponseEntity<List<Object[]>> searchMod(@RequestParam(value="query") String query) {
        List<Object[]> usernames = userService.searchUsersInfluencersAndMods(query);
        return ResponseEntity.ok(usernames);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/management/search/all/mods")
    public ResponseEntity<Page<String>> searchAllMods(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "5") int size) {
        Page<String> usernames = userService.searchAllMods(PageRequest.of(page, size));
        return ResponseEntity.ok(usernames);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/management/{username}/mod")
    public ResponseEntity<Map<String, String>> modUser(Model model, @PathVariable String username) {

        String response = userService.changeRole(username, "MOD");
        return ResponseEntity.ok(Map.of("roleStatus", response));
    }
}
