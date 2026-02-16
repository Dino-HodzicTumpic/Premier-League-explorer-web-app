package com.dino.plExplorer.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "goals")
@NoArgsConstructor @AllArgsConstructor @Getter @Setter @Builder
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scorer_id", nullable = false)
    private Player scorer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assistant_id") // Može biti null ako nema asistencije
    private Player assist;

    private Integer minute;

    @Column(name = "injury_time")
    private Integer injuryTime; // npr. 90 + 3

    private String type; // REGULAR, PENALTY, OWN_GOAL
}
