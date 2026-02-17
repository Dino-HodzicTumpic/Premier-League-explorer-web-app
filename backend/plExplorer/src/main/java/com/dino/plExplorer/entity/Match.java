package com.dino.plExplorer.entity;


import com.dino.plExplorer.entity.enums.MatchStatus;
import com.dino.plExplorer.entity.enums.MatchWinner;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Matches")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name = "external_id", nullable = false, unique = true)
    private Long externalId;

    @Column(name = "utc_date", nullable = false)
    private LocalDateTime utcDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MatchStatus status;

    @Column(nullable = false)
    private Integer matchday;

    private Double homeWinOdds;
    private Double drawOdds;
    private Double awayWinOdds;

    @Column(name = "home_score")
    private Integer homeScore;

    @Column(name = "away_score")
    private Integer awayScore;

    @Column(name = "home_score_halftime")
    private Integer homeScoreHalfTime;

    @Column(name = "away_score_halftime")
    private Integer awayScoreHalfTime;

    @Enumerated(EnumType.STRING)
    private MatchWinner winner;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "season_id", nullable = false)
    private Season season;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_team_id" , nullable = false)
    private Team homeTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "away_team_id", nullable = false)
    private Team awayTeam;

    private String stadium;

    private Long attendance;

    @Column(name = "injury_time")
    private Integer injuryTime;

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Goal> goals = new ArrayList<>();

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Booking> bookings = new ArrayList<>();

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MatchStatistic> statistics = new ArrayList<>();

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MatchAppearance> appearances = new ArrayList<>();

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Substitution> substitutions = new ArrayList<>();

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MatchReferee> referees = new ArrayList<>();



}
