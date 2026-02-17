package com.dino.plExplorer.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "team_coach_history")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TeamCoachHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "coach_id", nullable = false)
    private Coach coach;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @Column(name = "joined_at" , nullable = false)
    private LocalDate joinedAt;

    private LocalDate leftAt; // Nullable - ako je null, znači da je CURRENT coach

}
