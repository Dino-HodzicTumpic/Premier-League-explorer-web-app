import React from "react";
import type { EventPlayer, Team } from "../types/matches";
import { FaRegCircle } from "react-icons/fa";
import { cn } from "@/lib/utils";
import { getShortPlayerName } from "@/utils/getPlayerSurname";

type EventBookingProps = {
  team: Team;
  players: EventPlayer[];
  minute: number;
  card: "YELLOW" | "RED" | null;
  homeTeamTla: string;
};

export default function EventBooking({
  team,
  players,
  minute,
  card,
  homeTeamTla,
}: EventBookingProps) {
  const isHomeTeam = team.tla === homeTeamTla;

  return (
    <div
      className={cn(
        "flex  items-center gap-2",
        isHomeTeam ? "flex-row" : "flex-row-reverse",
      )}
    >
      <span className="">{minute}'</span>

      {card === "YELLOW" ? (
        <div className="h-4 w-3 rounded-xs bg-yellow-400 mx-2" />
      ) : (
        <div className="h-4 w-3 rounded-xs bg-red-500 mx-2" />
      )}
      <span>{getShortPlayerName(players[0].name)}</span>
    </div>
  );
}
