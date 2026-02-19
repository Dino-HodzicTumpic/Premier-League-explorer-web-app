package com.dino.plExplorer.dto.external.footballdata;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SquadMemberExternalData {

    private Long id;
    private String firstName;
    private String lastName;
    private String name;
    private String position;
    private LocalDate dateOfBirth;
    private String nationality;
    private Integer shirtNumber;
    private Long marketValue;
    private ContractExternalData contract;
}
