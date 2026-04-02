package com.dino.plExplorer.dto.external.espn.scoreboard;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EspnCompetition {
    private String id;
    private String startDate;
    private Integer attendance;
    private EspnStatus status;
    private EspnVenue venue;
    private List<EspnCompetitor> competitors;
    private List<EspnDetail> details;
}
