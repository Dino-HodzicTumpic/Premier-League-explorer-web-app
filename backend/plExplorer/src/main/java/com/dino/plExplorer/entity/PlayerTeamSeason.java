package com.dino.plExplorer.entity;

import com.dino.plExplorer.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "player_team_seasons", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_player_team_season",
                columnNames = {"player_id", "team_id", "season_id"}
        )
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PlayerTeamSeason extends BaseEntity {

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
