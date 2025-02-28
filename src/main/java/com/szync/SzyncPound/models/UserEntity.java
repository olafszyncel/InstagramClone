package com.szync.SzyncPound.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String fullName;
    private String email;
    private String password;

    // ROLES SYSTEM
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns= {@JoinColumn(name = "role_id", referencedColumnName = "id")}
    )
    @JsonManagedReference
    private List<Role> roles = new ArrayList<>();

    // POST SYSTEM:
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    @JsonBackReference
    private List<Post> posts = new ArrayList<>();

    // FOLLOW SYSTEM:
    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Follow> followings = new ArrayList<>();

    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Follow> followers = new ArrayList<>();
}


















