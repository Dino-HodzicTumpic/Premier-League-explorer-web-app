package com.dino.plExplorer.entity;

import com.dino.plExplorer.entity.enums.RefereeRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "match_referees")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MatchReferee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "referee_id", nullable = false)
    private Referee referee;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RefereeRole role;

}
