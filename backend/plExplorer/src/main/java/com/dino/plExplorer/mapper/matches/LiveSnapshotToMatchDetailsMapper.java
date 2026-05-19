package com.dino.plExplorer.mapper.matches;

import com.dino.plExplorer.config.mapper.MapStructConfig;
import com.dino.plExplorer.dto.response.matches.LiveMatchDetailsSnapshotDto;
import com.dino.plExplorer.dto.response.matches.MatchDetailsDto;
import com.dino.plExplorer.dto.response.matches.TeamDto;
import com.dino.plExplorer.entity.enums.MatchStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = MapStructConfig.class)
public interface LiveSnapshotToMatchDetailsMapper {

    @Mapping(target = "utcDate", ignore = true)
    @Mapping(target = "matchday", ignore = true)
    @Mapping(target = "winner", ignore = true)
    @Mapping(target = "stadium", ignore = true)
    @Mapping(target = "attendance", ignore = true)
    @Mapping(target = "referees", ignore = true)
    @Mapping(target = "matchEvents", source = "details")
    MatchDetailsDto toDto(LiveMatchDetailsSnapshotDto snapshot);

    default MatchStatus mapStatus(String status) {
        return LiveMatchDetailsMappingHelper.mapStatus(status);
    }

    default MatchDetailsDto.TeamDetailsDto toTeamDetailsDto(LiveMatchDetailsSnapshotDto.TeamDetailsDto team) {
        return LiveMatchDetailsMappingHelper.toTeamDetailsDto(team);
    }

    default MatchDetailsDto.TeamStatsDto toTeamStatsDto(List<LiveMatchDetailsSnapshotDto.StatisticDto> statistics) {
        return LiveMatchDetailsMappingHelper.toTeamStatsDto(statistics);
    }

    default MatchDetailsDto.MatchAppearanceDto toMatchAppearanceDto(LiveMatchDetailsSnapshotDto.RosterMemberDto member) {
        return LiveMatchDetailsMappingHelper.toMatchAppearanceDto(member);
    }

    default List<MatchDetailsDto.MatchAppearanceDto> toMatchAppearanceDtoList(List<LiveMatchDetailsSnapshotDto.RosterMemberDto> roster) {
        return LiveMatchDetailsMappingHelper.toMatchAppearanceDtoList(roster);
    }

    default MatchDetailsDto.MatchEventsDto toMatchEventsDto(LiveMatchDetailsSnapshotDto.DetailDto detail) {
        return LiveMatchDetailsMappingHelper.toMatchEventsDto(detail);
    }

    default List<MatchDetailsDto.MatchEventsDto> toMatchEventsDtoList(List<LiveMatchDetailsSnapshotDto.DetailDto> details) {
        return LiveMatchDetailsMappingHelper.toMatchEventsDtoList(details);
    }

    default MatchDetailsDto.EventPlayerDto toEventPlayerDto(LiveMatchDetailsSnapshotDto.ParticipantDto participant) {
        return LiveMatchDetailsMappingHelper.toEventPlayerDto(participant);
    }

    default List<MatchDetailsDto.EventPlayerDto> toEventPlayerDtoList(List<LiveMatchDetailsSnapshotDto.ParticipantDto> participants) {
        return LiveMatchDetailsMappingHelper.toEventPlayerDtoList(participants);
    }

    default TeamDto toTeamDto(LiveMatchDetailsSnapshotDto.DetailTeamDto team) {
        return LiveMatchDetailsMappingHelper.toTeamDto(team);
    }
}
