package com.dino.plExplorer.entity;

import com.dino.plExplorer.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "substitutions")
@NoArgsConstructor @AllArgsConstructor @Getter @Setter @Builder
public class Substitution extends BaseEntity {


    @ManyToOne( fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "player_in", nullable = false)
    private Player playerIn;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "player_out", nullable = false)
    private Player playerOut;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    private Integer minute;

    @Column(name = "injury_time")
    private Integer injuryTime; // Za zamjene u nadoknadi (npr. 90+2)

}
