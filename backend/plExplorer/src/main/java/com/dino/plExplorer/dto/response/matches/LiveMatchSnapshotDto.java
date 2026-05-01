package com.dino.plExplorer.dto.response.matches;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LiveMatchSnapshotDto {
    private String espnId;
    private Integer minute;
    private Integer injuryTime;
    private Integer homeScore;
    private Integer awayScore;
    private CachedMatchStatus status;

     public enum CachedMatchStatus {
        LIVE, ENDED_PENDING_PERSIST
    }
}
