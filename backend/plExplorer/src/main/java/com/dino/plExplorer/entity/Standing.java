package com.dino.plExplorer.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "standings", uniqueConstraints = {
        @UniqueConstraint(name = "uk_team_season_standing", columnNames = {"team_id", "season_id"})})
public class Standing {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private  Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "season_id", nullable = false)
    private Season season;

    @Column(nullable = false)
    private Integer position;

    @Column(name = "games_played",nullable = false)
    private Integer gamesPlayed = 0;

    @Column(nullable = false)
    private Integer won = 0;

    @Column(nullable = false)
    private Integer draw = 0;

    @Column(nullable = false)
    private Integer lost = 0;

    @Column(name = "goals_for", nullable = false)
    private Integer goalsFor = 0;

    @Column(name = "goals_against", nullable = false)
    private Integer goalsAgainst = 0;

    @Column(name = "goal_difference", nullable = false)
    private Integer goalDifference = 0;

    @Column(nullable = false)
    private Integer points = 0;

    @Column(length = 20)
    private String form; // npr. "D,W,W,W,W"

}
