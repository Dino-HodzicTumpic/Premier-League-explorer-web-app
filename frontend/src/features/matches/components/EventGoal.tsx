import React from "react";
import type { EventPlayer, Team } from "../types/matches";
import { FaFutbol } from "react-icons/fa";
import { GiGoalKeeper } from "react-icons/gi";
import { RiArrowGoBackLine } from "react-icons/ri";
import { cn } from "@/lib/utils";
import { getPlayerSurname, getShortPlayerName } from "@/utils/getPlayerSurname";

type EventGoalProps = {
  type: string;
  team: Team;
  players: EventPlayer[];
  minute: number;
  homeTeamTla: string;
};

export default function EventGoal({
  type,
  team,
  players,
  minute,
  homeTeamTla,
}: EventGoalProps) {
  const isHomeTeam = team.tla === homeTeamTla;

  if (type === "OWN_GOAL") {
    return (
      <div
        className={cn(
          "flex items-center gap-2",
          isHomeTeam ? "flex-row" : "flex-row-reverse",
        )}
      >
        <span className="">{minute}'</span>
        <span className="flex mx-2">
          <FaFutbol />
          <RiArrowGoBackLine />
        </span>
        <span>{getShortPlayerName(players[0].name)}</span>
      </div>
    );
  }

  if (type === "PENALTY_GOAL") {
    return (
      <div
        className={cn(
          "flex items-center gap-2",
          isHomeTeam ? "flex-row" : "flex-row-reverse",
        )}
      >
        <span className="">{minute}'</span>
        <GiGoalKeeper className="mx-2" />
        <span>{getShortPlayerName(players[0].name)}</span>
      </div>
    );
  }
  return (
    <div
      className={cn(
        "flex items-center gap-2",
        isHomeTeam ? "flex-row" : "flex-row-reverse",
      )}
    >
      <span className="">{minute}'</span>
      <FaFutbol className="mx-2" />
      <span>{getShortPlayerName(players[0].name)}</span>
      <span className="text-gray-400">
        {getShortPlayerName(players[1]?.name)}
      </span>
    </div>
  );
}
