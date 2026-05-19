import React from "react";
import type { MatchResult, MatchStatus, Team } from "../types/matches";
import { cn } from "@/lib/utils";
import { formatKickoff } from "@/utils.ts/formatKickoff";
import { useNavigate } from "react-router-dom";

type MatchCardProps = {
  espnMatchId?: string;
  injuryTime: number | null;
  minute?: number;
  status: MatchStatus;
  homeScore?: number;
  awayScore?: number;
  winner: MatchResult | null;
  homeTeam: Team;
  awayTeam: Team;
  kickoffTime: string;
};

export default function MatchCard({
  matchId,
  espnMatchId,
  injuryTime,
  minute,
  status,
  homeScore,
  awayScore,
  winner,
  homeTeam,
  awayTeam,
  kickoffTime,
}: MatchCardProps) {
  const navigate = useNavigate();

  const handleClick = () => {
    console.log("Navigating to match details for match ID:", espnMatchId);
    navigate(`/matches/${espnMatchId}/details`);
  };

  return (
    <div
      className="flex justify-between items-center   bg-white/90 backdrop-blur-md border border-white/20 rounded-xl  p-2 md:p-4 cursor-pointer hover:bg-white/95 transition-colors duration-200"
      onClick={handleClick}
    >
      <div className="flex flex-col pr-2 md:pr-4 shrink-0 ">
        <span>{formatKickoff(kickoffTime, status)}</span>
        {(status === "LIVE" || status === "IN_PLAY") && <span>{minute}'</span>}
        {status === "FINISHED" && <span>FT</span>}
        {status === "SCHEDULED" && (
          <span>
            {new Date(kickoffTime).toLocaleTimeString([], {
              hour: "2-digit",
              minute: "2-digit",
            })}
          </span>
        )}
      </div>
      <div className="flex flex-col flex-1 pl-2 md:pl-4 space-y-2 p-2 md:p-4 border-l border-gray-500 ">
        <div className="flex items-center justify-between p-1 md:p-2 ">
          <div className="flex items-center space-x-1 ">
            <img
              className="w-6 md:w-8 lg:w-10 "
              src={homeTeam.crestUrl}
              alt={homeTeam.name}
            />{" "}
            <span
              className={cn(
                "text-gray-500",
                winner === "HOME_TEAM" && "font-bold text-green-500",
              )}
            >
              {homeTeam.shortName}
            </span>
          </div>
          <span
            className={cn(
              "text-gray-500",
              winner === "HOME_TEAM" && "font-bold text-green-500",
            )}
          >
            {homeScore !== undefined ? homeScore : "-"}
          </span>
        </div>
        <div className="flex items-center justify-between p-1 md:p-2">
          <div className="flex items-center space-x-1 ">
            <img
              className="w-6 md:w-8 lg:w-10"
              src={awayTeam.crestUrl}
              alt={awayTeam.shortName}
            />{" "}
            <span
              className={cn(
                "text-gray-500",
                winner === "AWAY_TEAM" && "font-bold text-green-500",
              )}
            >
              {awayTeam.shortName}
            </span>
          </div>
          <span
            className={cn(
              "text-gray-500",
              winner === "AWAY_TEAM" && "font-bold text-green-500",
            )}
          >
            {awayScore !== undefined ? awayScore : "-"}
          </span>
        </div>
      </div>
    </div>
  );
}
