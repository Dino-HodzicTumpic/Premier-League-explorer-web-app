package com.dino.plExplorer.service.matches;

import com.dino.plExplorer.dto.external.footballdata.matches.MatchesResponse;
import com.dino.plExplorer.dto.response.matches.LiveMatchSnapshotDto;
import com.dino.plExplorer.dto.response.matches.MatchDto;
import com.dino.plExplorer.dto.response.matches.MatchListDto;
import com.dino.plExplorer.entity.Match;
import com.dino.plExplorer.mapper.matches.MatchListDtoMapper;
import com.dino.plExplorer.mapper.matches.MatchesMapper;
import com.dino.plExplorer.repository.MatchRepository;
import com.dino.plExplorer.repository.SeasonRepository;
import com.dino.plExplorer.service.FootballDataApiService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class MatchesService {
    private final FootballDataApiService footballDataApiService;
    private final MatchesMapper matchesMapper;
    private final SeasonRepository seasonRepository;
    private final MatchRepository matchRepository;
    private final MatchListDtoMapper matchListDtoMapper;
    private final CacheManager cacheManager;


    // TODO iz baze dohvatiti ako ima(a treba biti ako je vec odigrano to kolo),
    public List<MatchListDto> getMatches(int gameweek, int season){
        // 1. Dohvati sve matcheve iz baze za to kolo
        List<Match> allMatchesForGameweek = matchRepository.findByGameweekAndSeasonStartYear(gameweek, season);

        // 2. Mapiraj u DTO
        List<MatchListDto> matchListDtos = matchListDtoMapper.toDtoList(allMatchesForGameweek);


        // 3. Dopuni live podatke iz cachea samo za IN_PLAY matcheve

        Cache cache = cacheManager.getCache("liveMatchListCache");
        if (cache == null){
            log.warn("Live match list cache not found");
            return matchListDtos;
        }

        for(MatchListDto dto:  matchListDtos ) {
            String espnMatchId = dto.getEspnMatchId();

            LiveMatchSnapshotDto snapshot = cache.get(espnMatchId, LiveMatchSnapshotDto.class);
            if (snapshot != null){
                dto.setMinute(snapshot.getMinute());
                dto.setHomeScore(snapshot.getHomeScore());
                dto.setAwayScore(snapshot.getAwayScore());
                dto.setStatus("LIVE");

            }
        }

        return matchListDtos;
    }

    public List<MatchDto> getMatchesFromFootballDataApi(int gameweek, int season){
        Optional<MatchesResponse> external = footballDataApiService.getMatchesByGameWeek(gameweek, season);

        if(external.isEmpty()){
            log.error("Football data API service returned empty response for matches");
            return List.of();
        }

        return matchesMapper.toDtoList(external.get());
    }

    //TODO: implementirati dohvaćanje trenutnog gameweeka iz baze ili API-ja
    public Optional<Integer> getCurrentGameWeek(){
       return footballDataApiService.getCurrentGameWeekNumber();
    }



}
