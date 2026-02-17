package com.dino.plExplorer.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "seasons")
public class Season {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private  Long id;

    @Column(name = "external_id")
    private Long externalId;

    // format 2021/2022
    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "is_current")
    private boolean isCurrent = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "winner_team_id")
    private Team winner;

    @OneToMany(mappedBy = "season")
    private List<Standing> standings = new ArrayList<>();


}
