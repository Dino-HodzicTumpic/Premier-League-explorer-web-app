package com.dino.plExplorer.service.standings;

import com.dino.plExplorer.dto.external.footballdata.standings.StandingsResponse;
import com.dino.plExplorer.dto.external.footballdata.standings.TableEntry;
import com.dino.plExplorer.dto.response.standings.TeamStandingDto;
import com.dino.plExplorer.entity.Season;
import com.dino.plExplorer.entity.Standing;
import com.dino.plExplorer.entity.Team;
import com.dino.plExplorer.mapper.standings.StandingEntityToDtoMapper;
import com.dino.plExplorer.mapper.standings.StandingsEntityMapper;
import com.dino.plExplorer.mapper.standings.StandingsMapper;
import com.dino.plExplorer.repository.SeasonRepository;
import com.dino.plExplorer.repository.StandingRepository;
import com.dino.plExplorer.repository.TeamRepository;
import com.dino.plExplorer.service.FootballDataApiService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class StandingsService {
    private final FootballDataApiService footballDataApiService;
    private final StandingsMapper standingsMapper;
    private final StandingsEntityMapper standingsEntityMapper;
    private final SeasonRepository seasonRepository;
    private final TeamRepository teamRepository;
    private final StandingRepository standingRepository;
    private final StandingEntityToDtoMapper  standingEntityToDtoMapper;

    private static record SeasonInfo(String seasonName, int apiYear){}

    public List<TeamStandingDto> getCurrentStandings(){
        Optional<StandingsResponse> external = footballDataApiService.fetchStandings();

        if(external.isEmpty()){
            log.error("Football data API service returned empty response");
            return new ArrayList<>();
        }

        return standingsMapper.toDtoList(
            external.get().getStandings().getFirst().getTable()
        );
    }

    public List<TeamStandingDto> getSeasonStandings(String seasonName){
        List<Standing> standings = standingRepository.findBySeason_NameOrderByPositionAsc(seasonName);
        if(standings.isEmpty()){log.warn("Api returned empty response");}
        return standingEntityToDtoMapper.toDtoList(standings);
    }

    public void fetchAndSavePreviousSeasonsStandings() {
        List<SeasonInfo> seasonsToSave = List.of(
                new SeasonInfo("2023/2024",2023),
                new SeasonInfo("2024/2025", 2024)
        );

        for(SeasonInfo seasonInfo: seasonsToSave) {

            Optional<StandingsResponse> external = fetchStandingsFromApi(seasonInfo.apiYear());
            if (external.isEmpty()) return;


            Optional<Season> seasonOpt = fetchSeason(seasonInfo.seasonName());
            if (seasonOpt.isEmpty()) return;

            saveStandings(external.get().getStandings().getFirst().getTable(), seasonOpt.get());
        }

    }

    private Optional<StandingsResponse> fetchStandingsFromApi(int year) {
        Optional<StandingsResponse> response = footballDataApiService.fetchStandings(year);

        if (response.isEmpty()) {
            log.error("Football data API returned empty response for season 2023");

        }
        return response;
    }

    private Optional<Season> fetchSeason(String seasonName) {
        Optional<Season> seasonOpt = seasonRepository.findByName(seasonName);
        if (seasonOpt.isEmpty()) {
            log.error("Season {} not found in database", seasonName);
        }
        return seasonOpt;
    }

    private void saveStandings(List<TableEntry> tableEntries, Season season) {
        List<Standing> standings = new ArrayList<>();

        for (TableEntry entry : tableEntries) {
            Optional<Team> teamOpt = teamRepository.findByExternalId(entry.getTeam().getId());

            if (teamOpt.isEmpty()) {
                log.error("Team with externalId {} not found in database", entry.getTeam().getId());
                return;
            }

            standings.add(standingsEntityMapper.toEntity(entry, teamOpt.get(), season));
        }

        standingRepository.saveAll(standings);
        log.info("Successfully saved {} standings for season {}", standings.size(), season.getName());
    }

}
