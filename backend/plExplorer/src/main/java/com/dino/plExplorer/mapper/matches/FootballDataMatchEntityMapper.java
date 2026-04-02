package com.dino.plExplorer.mapper.matches;

import com.dino.plExplorer.config.mapper.MapStructConfig;
import com.dino.plExplorer.dto.response.matches.MatchDto;
import com.dino.plExplorer.entity.Match;
import com.dino.plExplorer.entity.Season;
import com.dino.plExplorer.entity.Team;
import com.dino.plExplorer.entity.enums.MatchStatus;
import com.dino.plExplorer.entity.enums.MatchWinner;
import org.mapstruct.*;


@Mapper(config = MapStructConfig.class, builder = @Builder(disableBuilder = true))
public interface FootballDataMatchEntityMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "externalId", source = "dto.id")
    @Mapping(target = "espnId", ignore = true)
    @Mapping(target = "utcDate", expression = "java(dto.getKickoffTime() != null ? dto.getKickoffTime().toLocalDateTime() : null)")
    @Mapping(target = "status", expression = "java(toMatchStatus(dto.getStatus()))")
    @Mapping(target = "matchday", source = "dto.matchday")
    @Mapping(target = "homeTeam", source = "homeTeam")
    @Mapping(target = "awayTeam", source = "awayTeam")
    @Mapping(target = "season", source = "season")
    @Mapping(target = "homeScore", source = "dto.score.fullTime.home")
    @Mapping(target = "awayScore", source = "dto.score.fullTime.away")
    @Mapping(target = "winner", expression = "java(toMatchWinner(dto.getScore() != null ? dto.getScore().getWinner() : null))")
    @Mapping(target = "homeWinOdds", ignore = true)
    @Mapping(target = "drawOdds", ignore = true)
    @Mapping(target = "awayWinOdds", ignore = true)
    @Mapping(target = "homeScoreHalfTime", ignore = true)
    @Mapping(target = "awayScoreHalfTime", ignore = true)
    @Mapping(target = "stadium", ignore = true)
    @Mapping(target = "attendance", ignore = true)
    @Mapping(target = "injuryTime", ignore = true)
    @Mapping(target = "goals", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    @Mapping(target = "statistics", ignore = true)
    @Mapping(target = "appearances", ignore = true)
    @Mapping(target = "substitutions", ignore = true)
    @Mapping(target = "referees", ignore = true)

    Match toEntity(MatchDto dto, Team homeTeam, Team awayTeam, Season season);

    @InheritConfiguration(name = "toEntity")
    @Mapping(target = "id", ignore = true)
    void updateEntity(MatchDto dto, Team homeTeam, Team awayTeam, Season season, @MappingTarget Match match);

    default MatchStatus toMatchStatus(String status) {
        if (status == null || status.isBlank()) {
            return MatchStatus.SCHEDULED;
        }
        try {
            return MatchStatus.valueOf(status);
        } catch (IllegalArgumentException ignored) {
            return MatchStatus.SCHEDULED;
        }
    }

    default MatchWinner toMatchWinner(MatchDto.MatchResult winner) {
        if (winner == null) {
            return null;
        }
        return MatchWinner.valueOf(winner.name());
    }
}

