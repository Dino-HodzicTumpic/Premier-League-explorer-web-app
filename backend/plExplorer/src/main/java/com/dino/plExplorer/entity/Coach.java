package com.dino.plExplorer.entity;

import com.dino.plExplorer.entity.base.BaseEntity;
import com.dino.plExplorer.entity.interfaces.HasImage;
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
public class Coach extends BaseEntity implements HasImage {

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

    @Column(name = "birth_date")
    private LocalDate dateOfBirth;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "image_public_id", length = 255)
    private String imagePublicId;

}
