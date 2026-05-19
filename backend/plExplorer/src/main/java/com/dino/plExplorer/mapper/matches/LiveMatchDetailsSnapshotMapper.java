package com.dino.plExplorer.mapper.matches;

import com.dino.plExplorer.config.mapper.MapStructConfig;
import com.dino.plExplorer.dto.external.espn.scoreboard.EspnStatistic;
import com.dino.plExplorer.dto.external.espn.summary.EspnSummaryResponse;
import com.dino.plExplorer.dto.response.matches.LiveMatchDetailsSnapshotDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Mapper(config = MapStructConfig.class)
public interface LiveMatchDetailsSnapshotMapper {

    default LiveMatchDetailsSnapshotDto toSnapshot(EspnSummaryResponse summary, String espnMatchId) {
        if (summary == null) {
            return null;
        }

        return LiveMatchDetailsSnapshotDto.builder()
                .espnMatchId(espnMatchId)
                .homeTeam(mapTeam(summary, "home"))
                .awayTeam(mapTeam(summary, "away"))
                .details(toDetailDtoList(extractDetails(summary)))
                .build();
    }

    default LiveMatchDetailsSnapshotDto.TeamDetailsDto mapTeam(EspnSummaryResponse summary, String homeAway) {
        EspnSummaryResponse.RosterData rosterData = findRosterByHomeAway(summary, homeAway);
        EspnSummaryResponse.TeamStats teamStats = findTeamStatsByHomeAway(summary, homeAway);

        EspnSummaryResponse.Team team = rosterData != null && rosterData.getTeam() != null
                ? rosterData.getTeam()
                : (teamStats != null ? teamStats.getTeam() : null);

        return LiveMatchDetailsSnapshotDto.TeamDetailsDto.builder()
                .espnTeamId(team != null ? team.getEspnId() : null)
                .tla(team != null ? team.getTla() : null)
                .shortName(team != null ? team.getShortName() : null)
                .homeAway(homeAway)
                .formation(rosterData != null ? rosterData.getFormation() : null)
                .statistics(toStatisticDtoList(teamStats != null ? teamStats.getStatistics() : null))
                .roster(toRosterMemberDtoList(rosterData != null ? rosterData.getRoster() : null))
                .build();
    }

    default EspnSummaryResponse.RosterData findRosterByHomeAway(EspnSummaryResponse summary, String homeAway) {
        if (summary == null || summary.getRosters() == null || summary.getRosters().isEmpty()) {
            return null;
        }

        return summary.getRosters().stream()
                .filter(Objects::nonNull)
                .filter(r -> homeAway.equalsIgnoreCase(r.getHomeAway()))
                .findFirst()
                .orElse(null);
    }

    default EspnSummaryResponse.TeamStats findTeamStatsByHomeAway(EspnSummaryResponse summary, String homeAway) {
        if (summary == null || summary.getBoxscore() == null || summary.getBoxscore().getTeams() == null) {
            return null;
        }

        return summary.getBoxscore().getTeams().stream()
                .filter(Objects::nonNull)
                .filter(t -> homeAway.equalsIgnoreCase(t.getHomeAway()))
                .findFirst()
                .orElse(null);
    }

    default List<EspnSummaryResponse.Detail> extractDetails(EspnSummaryResponse summary) {
        if (summary == null || summary.getHeader() == null || summary.getHeader().getCompetitions() == null) {
            return List.of();
        }

        return summary.getHeader().getCompetitions().stream()
                .filter(Objects::nonNull)
                .map(EspnSummaryResponse.Competition::getDetails)
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .filter(Objects::nonNull)
                .toList();
    }

    @Mapping(target = "minute", source = "clock.minute")
    LiveMatchDetailsSnapshotDto.DetailDto toDetailDto(EspnSummaryResponse.Detail detail);

    List<LiveMatchDetailsSnapshotDto.DetailDto> toDetailDtoList(List<EspnSummaryResponse.Detail> details);

    @Mapping(target = "playerEspnId", source = "athlete.espnId")
    @Mapping(target = "fullName", source = "athlete.fullName")
    LiveMatchDetailsSnapshotDto.ParticipantDto toParticipantDto(EspnSummaryResponse.Participant participant);

    List<LiveMatchDetailsSnapshotDto.ParticipantDto> toParticipantDtoList(List<EspnSummaryResponse.Participant> participants);

    @Mapping(target = "playerEspnId", source = "athlete.espnId")
    @Mapping(target = "fullName", source = "athlete.fullName")
    @Mapping(target = "position", source = "position.name")
    @Mapping(target = "starter", source = "isStarter")
    LiveMatchDetailsSnapshotDto.RosterMemberDto toRosterMemberDto(EspnSummaryResponse.RosterMember member);

    default List<LiveMatchDetailsSnapshotDto.RosterMemberDto> toRosterMemberDtoList(List<EspnSummaryResponse.RosterMember> roster) {
        if (roster == null || roster.isEmpty()) {
            return Collections.emptyList();
        }
        return roster.stream().map(this::toRosterMemberDto).toList();
    }

    LiveMatchDetailsSnapshotDto.DetailTeamDto toDetailTeamDto(EspnSummaryResponse.Team team);

    LiveMatchDetailsSnapshotDto.StatisticDto toStatisticDto(EspnStatistic statistic);

    default List<LiveMatchDetailsSnapshotDto.StatisticDto> toStatisticDtoList(List<EspnStatistic> statistics) {
        if (statistics == null || statistics.isEmpty()) {
            return Collections.emptyList();
        }
        return statistics.stream().map(this::toStatisticDto).toList();
    }
}

