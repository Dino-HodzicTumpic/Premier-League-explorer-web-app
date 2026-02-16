package com.dino.plExplorer.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
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





}
