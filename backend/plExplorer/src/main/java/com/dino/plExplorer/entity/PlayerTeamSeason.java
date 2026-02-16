package com.dino.plExplorer.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "player_team_seasons")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PlayerTeamSeason {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "season_id", nullable = false)
    private Season season;

}
