package com.szync.SzyncPound.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "follows", uniqueConstraints = {@UniqueConstraint(columnNames = {"follower_id", "following_id"})})
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "follower", nullable = false)
    private UserEntity follower; // kto obserwuje

    @ManyToOne
    @JoinColumn(name = "following", nullable = false)
    private UserEntity following; // kto jest obserwowany

    @CreationTimestamp
    private LocalDateTime createdOn;
}
