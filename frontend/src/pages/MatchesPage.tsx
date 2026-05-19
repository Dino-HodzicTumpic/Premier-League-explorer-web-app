import MatchCard from "@/features/matches/components/MatchCard";
import MatchWeekSelector from "@/features/matches/components/MatchWeekSelector";
import { useCurrentMatchWeek } from "@/features/matches/hooks/useCurrentMatchWeek";
import { useMatchesByGameweek } from "@/features/matches/hooks/useMatchesByGameweek";
import React, { useEffect, useState } from "react";

export default function MatchesPage() {
  const {
    data: currentMatchWeek,
    isLoading,
    isError,
    error,
  } = useCurrentMatchWeek();

  const [gameWeekSelected, setGameWeekSelected] = useState<number>(35);

  const {
    data: matchList,
    isLoading: isMatchesLoading,
    isError: isMatchesError,
    error: matchesError,
  } = useMatchesByGameweek(gameWeekSelected);

  const liveMatches = matchList?.filter((m) => m.status === "LIVE") ?? [];
  const finishedMatches =
    matchList?.filter((m) => m.status === "FINISHED") ?? [];
  const upcomingMatches =
    matchList?.filter((m) => m.status === "SCHEDULED") ?? [];

  useEffect(() => {
    if (currentMatchWeek && gameWeekSelected == 35) {
      setGameWeekSelected(currentMatchWeek);
    }
  }, [currentMatchWeek]);

  return (
    <div className="md:w-1/2 mb-10">
      <MatchWeekSelector
        gameWeekSelected={gameWeekSelected}
        setGameWeekSelected={setGameWeekSelected}
      />
      {isMatchesLoading && <p>Loading matches...</p>}
      {isMatchesError && <p>Error loading matches: {matchesError.message}</p>}
      {liveMatches.length > 0 && (
        <div className="">
          <h2>Live Matches</h2>
          <div className="flex-col space-y-4">
            {liveMatches.map((match) => (
              <MatchCard
                key={match.espnMatchId}
                injuryTime={match.injuryTime}
                minute={match.minute}
                status={match.status}
                homeScore={match.homeScore}
                awayScore={match.awayScore}
                winner={match.winner}
                homeTeam={match.homeTeam}
                awayTeam={match.awayTeam}
                kickoffTime={match.kickoffTime}
                espnMatchId={match.espnMatchId}
              />
            ))}
          </div>
        </div>
      )}
      {finishedMatches.length > 0 && (
        <div className="">
          <h2>Finished Matches</h2>
          <div className="flex-col space-y-4">
            {finishedMatches.map((match) => (
              <MatchCard
                key={match.espnMatchId}
                injuryTime={match.injuryTime}
                minute={match.minute}
                status={match.status}
                homeScore={match.homeScore}
                awayScore={match.awayScore}
                winner={match.winner}
                homeTeam={match.homeTeam}
                awayTeam={match.awayTeam}
                kickoffTime={match.kickoffTime}
                espnMatchId={match.espnMatchId}
              />
            ))}
          </div>
        </div>
      )}
      {upcomingMatches.length > 0 && (
        <div>
          <h2>Upcoming Matches</h2>
          <div className="flex-col space-y-4">
            {upcomingMatches.map((match) => (
              <MatchCard
                key={match.espnMatchId}
                injuryTime={match.injuryTime}
                minute={match.minute}
                status={match.status}
                winner={match.winner}
                homeTeam={match.homeTeam}
                awayTeam={match.awayTeam}
                kickoffTime={match.kickoffTime}
                espnMatchId={match.espnMatchId}
              />
            ))}
          </div>
        </div>
      )}
    </div>
  );
}
