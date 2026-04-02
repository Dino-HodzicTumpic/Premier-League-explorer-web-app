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
    private Integer cornerKicks = null;

    @Column(name = "goal_kicks")
    private Integer goalKicks = null;
    private Integer offsides = null;
    private Integer fouls = null;

    @Column(name = "ball_possession")
    private Integer ballPossession = null;

    @Column(name = "accurate_passes")
    private Integer accuratePasses = null;

    @Column(name = "total_passes")
    private Integer totalPasses = null;

    private Integer saves = null;

    @Column(name = "throw_ins")
    private Integer throwIns = null;

    private Integer shots = null;
    @Column(name = "shots_on_goal")
    private Integer shotsOnGoal = null;

    @Column(name = "shots_off_goal")
    private Integer shotsOffGoal = null;

    @Column(name = "yellow_cards")
    private Integer yellowCards = null;

    @Column(name = "red_cards")
    private Integer redCards = null;

    @Column(name = "total_bookings")
    private Integer totalBookings = null;



}
