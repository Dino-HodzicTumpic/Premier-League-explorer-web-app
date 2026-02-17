package com.dino.plExplorer.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "referees")
@NoArgsConstructor @AllArgsConstructor @Getter @Setter @Builder
public class Referee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "external_id", nullable = false, unique = true)
    private Long externalId;

    @Column(nullable = false)
    private String name;
    private String nationality;
}
