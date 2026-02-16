package com.dino.plExplorer.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "match_statistics")
public class MatchStatistic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id")
    private Match match;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Column(name = "corner_kicks")
    private Integer cornerKicks;

    @Column(name = "goal_kicks")
    private Integer goalKicks;
    private Integer offsides;
    private Integer fouls;

    @Column(name = "ball_possession")
    private Integer ballPossession;

    private Integer saves;

    @Column(name = "throw_ins")
    private Integer throwIns;

    private Integer shots;
    @Column(name = "shots_on_goal")
    private Integer shotsOnGoal;

    @Column(name = "shots_off_goal")
    private Integer shotsOffGoal;

    @Column(name = "yellow_cards")
    private Integer yellowCards;

    @Column(name = "red_cards")
    private Integer redCards;

    @Column(name = "total_bookings")
    private Integer totalBookings;



}
