package com.dino.plExplorer.service.matches;

import com.dino.plExplorer.dto.external.footballdata.matches.MatchesResponse;
import com.dino.plExplorer.dto.response.matches.*;
import com.dino.plExplorer.entity.Match;
import com.dino.plExplorer.entity.Team;
import com.dino.plExplorer.mapper.matches.FinishedMatchDetailsMapper;
import com.dino.plExplorer.mapper.matches.LiveSnapshotToMatchDetailsMapper;
import com.dino.plExplorer.mapper.matches.MatchListDtoMapper;
import com.dino.plExplorer.mapper.matches.MatchesMapper;
import com.dino.plExplorer.repository.MatchRepository;
import com.dino.plExplorer.repository.TeamRepository;
import com.dino.plExplorer.service.FootballDataApiService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class MatchesService {
    private final FootballDataApiService footballDataApiService;
    private final MatchesMapper matchesMapper;
    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final MatchListDtoMapper matchListDtoMapper;
    private final LiveSnapshotToMatchDetailsMapper liveSnapshotToMatchDetailsMapper;
    private final FinishedMatchDetailsMapper finishedMatchDetailsMapper;
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

    @Transactional(readOnly = true)
    public Optional<MatchDetailsDto> getMatchDetails(String espnMatchId){
        // 1 provjeri live details cache
        Cache detailsCache = cacheManager.getCache("liveMatchDetailsCache");
        if (detailsCache != null){
            LiveMatchDetailsSnapshotDto cached = detailsCache.get(espnMatchId, LiveMatchDetailsSnapshotDto.class);
            if (cached != null) {
                MatchDetailsDto dto = liveSnapshotToMatchDetailsMapper.toDto(cached);
                enrichTeamCrestUrls(dto);
                return Optional.of(dto);
            }
        }

        // 2 ako nema u cacheu provjeri u DB da li postoji i završena
         Optional<Match> matchOpt = matchRepository.findWithGoals(espnMatchId);
        if(matchOpt.isEmpty()) return Optional.empty();

        Match persistedMatch = matchOpt.get();

        matchRepository.findWithBookings(espnMatchId);
        matchRepository.findWithSubs(espnMatchId);
        matchRepository.findWithAppearances(espnMatchId);
        matchRepository.findWithReferees(espnMatchId);
        matchRepository.findWithStats(espnMatchId);


        MatchDetailsDto dto = finishedMatchDetailsMapper.toDto(persistedMatch);
        return Optional.of(dto);


        // 3 fallback dohvat sa espn api

    }


    private void enrichTeamCrestUrls(MatchDetailsDto dto) {
        if (dto == null) {
            return;
        }

        enrichTeamCrestUrl(dto.getHomeTeam());
        enrichTeamCrestUrl(dto.getAwayTeam());
    }

    private void enrichTeamCrestUrl(MatchDetailsDto.TeamDetailsDto teamDetailsDto) {
        if (teamDetailsDto == null || teamDetailsDto.getEspnTeamId() == null) {
            return;
        }

        if (teamDetailsDto.getCrestUrl() != null && !teamDetailsDto.getCrestUrl().isBlank()) {
            return;
        }

        teamRepository.findByEspnId(teamDetailsDto.getEspnTeamId())
                .map(Team::getCrestUrl)
                .ifPresent(teamDetailsDto::setCrestUrl);
    }


}
