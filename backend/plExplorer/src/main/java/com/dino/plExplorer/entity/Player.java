package com.dino.plExplorer.entity;

import com.dino.plExplorer.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "players")
public class Player extends BaseEntity {


    @Column(name = "external_id", nullable = false, unique = true)
    private Long externalId;

    @Column(nullable = false)
    private String name;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private  String lastName;

    private String nationality;

    @Column(name= "shirt_number")
    private Integer shirtNumber;

    @Column(name = "birth_date")
    private LocalDate dateOfBirth;

    @Column(name = "contract_until")
    private LocalDate contractUntil;

    @Column(name = "contract_start")
    private LocalDate contractStart;

    @Column(name = "market_value")
    private Long marketValue;

    @Column( length = 50)
    private String position;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY )
    @JoinColumn(name = "current_team_id")
    private Team currentTeam;


}
