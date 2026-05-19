import type { Match } from "date-fns";
import React from "react";
import type { MatchStatus, MatchWinner } from "../types/matches";
import { cn } from "@/lib/utils";

type MatchHeaderScoreProps = {
  homeScore?: number;
  awayScore?: number;
  minute?: number;
  injuryTime?: number | null;
  status: MatchStatus;
  winner?: MatchWinner;
  utcDate: string;
};

export default function MatchHeaderScore({
  homeScore,
  awayScore,
  minute,
  injuryTime,
  status,
  winner,
  utcDate,
}: MatchHeaderScoreProps) {
  if (status === "LIVE" || status === "IN_PLAY") {
    return (
      <div className="flex flex-col items-center  text-red-500">
        <span>{minute}'</span>
        {injuryTime && <span>+{injuryTime}'</span>}
        <span>
          {homeScore || 0} - {awayScore || 0}
        </span>
      </div>
    );
  }

  if (status === "FINISHED") {
    return (
      <div className="flex flex-col items-center text-gray-400">
        <div>
          <span
            className={cn("md:text-xl", winner === "HOME_TEAM" && "text-black")}
          >
            {homeScore}
          </span>
          <span>-</span>
          <span
            className={cn("md:text-xl", winner === "AWAY_TEAM" && "text-black")}
          >
            {awayScore}
          </span>
        </div>
        <span className="text-gray-400">Finished</span>
      </div>
    );
  }

  const date = new Date(utcDate);
  const isToday = new Date().toDateString() === date.toDateString();
  if (isToday) {
    return (
      <div className="flex flex-col items-center">
        <span className="font-bold">
          {date.toLocaleTimeString([], { hour: "2-digit", minute: "2-digit" })}
        </span>
        <span className="text-sm text-gray-500">Today</span>
      </div>
    );
  }

  return (
    <div className="flex flex-col items-center">
      <span className="font-bold">{date.toLocaleDateString()}</span>
      <span className="text-sm text-gray-500">
        {date.toLocaleTimeString([], { hour: "2-digit", minute: "2-digit" })}
      </span>
    </div>
  );
}
