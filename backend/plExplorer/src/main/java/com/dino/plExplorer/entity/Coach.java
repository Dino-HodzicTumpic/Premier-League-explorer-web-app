package com.dino.plExplorer.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "coaches")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Coach {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @Column(name = "external_id", nullable = false, unique = true)
    private Long externalId;

    @OneToOne(mappedBy = "currentCoach", fetch = FetchType.LAZY)
    private Team currentTeam;

    @Column(nullable = false, length = 150)
    private String name; //firstName + lastName

    private String nationality;

    @Column(name = "contract_start")
    private LocalDate contractStart;

    @Column(name = "contract_until")
    private LocalDate contractUntil;

}
