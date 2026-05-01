package com.dino.plExplorer.dto.response.matches;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
public class MatchListDto {

    private Long matchId;
    private String espnMatchId;
    private Integer minute;
    private Integer injuryTime;
    private String status; // "LIVE", "SCHECHULED, "FINISHED"
    private MatchDto.MatchResult winner;
    private OffsetDateTime kickoffTime;
    private Integer homeScore;
    private Integer awayScore;
    private MatchDto.TeamDto homeTeam;
    private MatchDto.TeamDto awayTeam;
}
