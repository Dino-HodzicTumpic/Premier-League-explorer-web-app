package com.dino.plExplorer.entity;

import com.dino.plExplorer.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "referees")
@NoArgsConstructor @AllArgsConstructor @Getter @Setter @Builder
public class Referee extends BaseEntity {


    @Column(name = "external_id", nullable = false, unique = true)
    private Long externalId;

    @Column(nullable = false)
    private String name;
    private String nationality;
}
