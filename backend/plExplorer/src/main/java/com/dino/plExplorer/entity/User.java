package com.dino.plExplorer.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String firstName;
    private String lastName;
    private String avatarUrl;

    // Google OAuth2 specifično polje - jedinstveni ID korisnika na Googleu
    @Column(name = "google_id", unique = true)
    private String googleId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;



}
