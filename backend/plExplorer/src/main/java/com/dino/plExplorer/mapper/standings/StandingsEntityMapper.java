package com.dino.plExplorer.mapper.standings;

import com.dino.plExplorer.config.mapper.MapStructConfig;
import com.dino.plExplorer.dto.external.footballdata.standings.TableEntry;
import com.dino.plExplorer.entity.Season;
import com.dino.plExplorer.entity.Standing;
import com.dino.plExplorer.entity.Team;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface StandingsEntityMapper {


    @Mapping(target = "team", source = "team")
    @Mapping(target = "season", source = "season")
    @Mapping(target = "position", source = "entry.position")
    @Mapping(target = "gamesPlayed", source = "entry.playedGames")
    @Mapping(target = "won", source = "entry.won")
    @Mapping(target = "draw", source = "entry.draw")
    @Mapping(target = "lost", source = "entry.lost")
    @Mapping(target = "goalsFor", source = "entry.goalsFor")
    @Mapping(target = "goalsAgainst", source = "entry.goalsAgainst")
    @Mapping(target = "goalDifference", source = "entry.goalDifference")
    @Mapping(target = "points", source = "entry.points")
    @Mapping(target = "form", source = "entry.form")
    Standing toEntity(TableEntry entry, Team team, Season season);
}
