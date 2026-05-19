import MatchDetailsTabs from "@/features/matches/components/MatchDetailsTabs";
import MatchHeader from "@/features/matches/components/MatchHeader";
import { useMatchDetails } from "@/features/matches/hooks/useMatchDetails";
import type { TeamDetails } from "@/features/matches/types/matches";
import React from "react";
import { useParams } from "react-router";

export default function MatchDetailsPage() {
  const { espnMatchId } = useParams();
  const {
    data: matchDetails,
    isLoading,
    isError,
    error,
  } = useMatchDetails(espnMatchId!);

  if (isLoading) {
    return <div>Loading...</div>;
  }

  if (isError) {
    return (
      <div>
        Error: {error instanceof Error ? error.message : "Unknown error"}
      </div>
    );
  }

  if (!matchDetails) {
    return <div>No match details found.</div>;
  }

  return (
    <div className="w-full md:w-3/4 z-10 ">
      <MatchHeader
        minute={matchDetails.minute}
        status={matchDetails.status}
        injuryTime={matchDetails.injuryTime}
        utcDate={matchDetails.utcDate}
        matchday={matchDetails.matchday}
        winner={matchDetails.winner}
        stadium={matchDetails.stadium}
        attendance={matchDetails.attendance}
        matchEvents={matchDetails.matchEvents}
        referees={matchDetails.referees}
        homeTeam={matchDetails.homeTeam}
        awayTeam={matchDetails.awayTeam}
      />
      <MatchDetailsTabs matchDetails={matchDetails} />
    </div>
  );
}
