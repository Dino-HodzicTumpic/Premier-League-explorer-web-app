package com.dino.plExplorer.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "teams")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "external_Id")
    private Long externalId;

    private String name;

    @Column(name = "short_name")
    private String shortName;

    private String stadium;

    @Column(name = "crest_url")
    private String crestUrl;

    @Column(name = "year_founded")
    private Integer yearFounded;

    @Column(name = "club_colors")
    private String clubColors;

    private String tla;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_coach_id")
    private Coach currentCoach;

    @OneToMany(mappedBy = "currentTeam")
    List<Player> currentPlayers = new ArrayList<>();

}
