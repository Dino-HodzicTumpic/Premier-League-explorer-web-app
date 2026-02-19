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
@Table(name = "match_appearances")
public class MatchAppearance extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @Column(name = "shirt_number" , nullable = false)
    private Integer shirtNumber;

    @Column(nullable = false)
    private String position;

    @Column(name = "is_starting", nullable = false)
    private boolean isStarting;


}
