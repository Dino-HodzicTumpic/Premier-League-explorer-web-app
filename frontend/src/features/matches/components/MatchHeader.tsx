import React from "react";
import type {
  MatchEvent,
  MatchReferee,
  MatchStatus,
  MatchWinner,
  TeamDetails,
} from "../types/matches";
import MatchHeaderScore from "./MatchHeaderScore";

type MatchHeaderProps = {
  minute: number | null;
  status: MatchStatus;

  injuryTime: number | null;
  utcDate: string; // LocalDateTime → ISO string
  matchday: number;

  winner: MatchWinner | null;

  stadium: string;
  attendance: number | null;

  matchEvents: MatchEvent[];
  referees: MatchReferee[];

  homeTeam: TeamDetails;
  awayTeam: TeamDetails;
};

export default function MatchHeader({
  minute,
  status,
  injuryTime,
  utcDate,
  matchday,
  winner,
  stadium,
  attendance,
  matchEvents,
  referees,
  homeTeam,
  awayTeam,
}: MatchHeaderProps) {
  return (
    <div className="flex flex-col justify-center items-center rounded-md p-2 bg-white">
      <div className="flex w-full justify-center items-center gap-4 mb-6">
        <span>Premier League</span>{" "}
        <div className="rounded-full bg-blue-500 w-2 h-2 mt-1"></div>
        <span>Round {matchday}</span>
      </div>
      <div className="flex justify-between w-3/4">
        <div className="flex flex-col items-center">
          <img
            className="w-12"
            src={homeTeam.crestUrl}
            alt={homeTeam.shortName}
          />
          <span>{homeTeam.shortName}</span>
        </div>
        <MatchHeaderScore
          homeScore={homeTeam.score}
          awayScore={awayTeam.score}
          minute={minute ?? undefined}
          injuryTime={injuryTime}
          status={status}
          winner={winner ?? undefined}
          utcDate={utcDate}
        />
        <div className="flex flex-col items-center">
          <img
            className="w-12"
            src={awayTeam.crestUrl}
            alt={awayTeam.shortName}
          />
          <span>{awayTeam.shortName}</span>
        </div>
      </div>
      <div></div>
    </div>
  );
}
