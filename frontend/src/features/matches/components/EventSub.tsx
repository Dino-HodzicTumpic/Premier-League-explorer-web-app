import React from "react";
import { ArrowRightLeft } from "lucide-react";
import type { EventPlayer, Team } from "../types/matches";
import { getShortPlayerName } from "@/utils.ts/getPlayerSurname";
import { cn } from "@/lib/utils";

type EventSubProps = {
  team: Team;
  players: EventPlayer[];
  minute: number;
  homeTeamTla: string;
};

export default function EventSub({
  team,
  players,
  minute,
  homeTeamTla,
}: EventSubProps) {
  const isHomeTeam = team.tla === homeTeamTla;

  return (
    <div
      className={cn(
        "flex items-center gap-2",
        isHomeTeam ? "flex-row" : "flex-row-reverse",
      )}
    >
      <span className="">{minute}'</span>
      <ArrowRightLeft className="mx-2" />
      <span>{getShortPlayerName(players[1].name)}</span>
      <span className="text-gray-400">
        {getShortPlayerName(players[0].name)}
      </span>
    </div>
  );
}
