package com.dino.plExplorer.dto.response.matches;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LiveMatchDetailsSnapshotDto {

    private String espnMatchId;
    private Integer minute;
    private Integer injuryTime;
    private String status;


    private TeamDetailsDto homeTeam;
    private TeamDetailsDto awayTeam;
    private List<DetailDto> details;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeamDetailsDto {
        private String espnTeamId;
        private String tla;
        private String shortName;
        private String crestUrl;
        private String homeAway;
        private String formation;
        private List<StatisticDto> statistics;
        private List<RosterMemberDto> roster;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatisticDto {
        private String name;
        private String displayValue;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RosterMemberDto {
        private String playerEspnId;
        private String fullName;
        private String jersey;
        private String position;
        private Boolean starter;
        private Boolean subbedIn;
        private Boolean subbedOut;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetailDto {
        private Boolean scoringPlay;
        private String minute;
        private DetailTeamDto team;
        private List<ParticipantDto> participants;
        private Boolean redCard;
        private Boolean ownGoal;
        private Boolean penaltyKick;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetailTeamDto {
        private String espnTeamId;
        private String tla;
        private String shortName;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ParticipantDto {
        private String playerEspnId;
        private String fullName;
    }
}
