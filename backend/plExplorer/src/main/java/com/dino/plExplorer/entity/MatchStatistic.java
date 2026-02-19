package com.dino.plExplorer.entity;

import com.dino.plExplorer.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "match_statistics")
public class MatchStatistic extends BaseEntity {


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @Column(name = "corner_kicks")
    private Integer cornerKicks = 0;

    @Column(name = "goal_kicks")
    private Integer goalKicks = 0;
    private Integer offsides = 0;
    private Integer fouls = 0;

    @Column(name = "ball_possession")
    private Integer ballPossession = 0;

    private Integer saves = 0;

    @Column(name = "throw_ins")
    private Integer throwIns = 0;

    private Integer shots = 0;
    @Column(name = "shots_on_goal")
    private Integer shotsOnGoal = 0;

    @Column(name = "shots_off_goal")
    private Integer shotsOffGoal = 0;

    @Column(name = "yellow_cards")
    private Integer yellowCards = 0;

    @Column(name = "red_cards")
    private Integer redCards = 0;

    @Column(name = "total_bookings")
    private Integer totalBookings = 0;



}
