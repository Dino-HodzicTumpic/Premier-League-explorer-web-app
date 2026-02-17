package com.dino.plExplorer.entity;

import com.dino.plExplorer.entity.enums.CardType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bookings")
@NoArgsConstructor @AllArgsConstructor @Getter @Setter @Builder
public class Booking {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private  Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @Column(nullable = false)
    private Integer minute;

    @Enumerated(EnumType.STRING)
    @Column(name = "card_type", nullable = false, length = 20)
    private CardType card; // Yellow, Red

}
