package com.dino.plExplorer.dto.response.topScorers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopScorersDto {
    private Long   externalId;
    private String playerName;
    private String playerPosition;
    private String playerNationality;
    private String playerImageUrl;
    private String teamName;
    private String teamShortName;
    private String teamTla;
    private String teamCrestUrl;

    private Integer numberOfGoals;
}
