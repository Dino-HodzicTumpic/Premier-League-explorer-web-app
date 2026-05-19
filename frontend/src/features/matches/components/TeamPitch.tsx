import React, { use, useEffect } from "react";
import type { MatchAppearance } from "../types/matches";
import Player from "./Player";

type TeamPitchProps = {
  starters: MatchAppearance[];
  teamName: string;
  isHomeTeam: boolean;
};

export default function TeamPitch({
  starters,
  teamName,
  isHomeTeam,
}: TeamPitchProps) {
  return (
    <div
      className={`relative h-96 flex-1 soccer-pitch min-h-100 border-green-500/30 ${
        !isHomeTeam ? "border-t-2 lg:border-t-0 lg:border-l-2" : ""
      }`}
    >
      {/* Centar kruga */}
      <div
        className={`absolute w-24 h-24 border-2 border-white/20 rounded-full left-1/2 -translate-x-1/2 ${
          !isHomeTeam ? "-top-12" : "-bottom-12"
        }`}
      />

      {/* Igrači */}
      <div className={`w-full h-full  ${!isHomeTeam ? "" : "rotate-180"}`}>
        {starters.map((starter, idx) => (
          <Player
            key={idx}
            name={starter.playerName}
            position={starter.position}
            shirtNumber={starter.shirtNumber}
            isHomeTeam={isHomeTeam}
          />
        ))}
      </div>

      {/* Ime tima */}
      <div className="absolute bottom-2 left-2 text-white/50 text-xs font-bold uppercase">
        {teamName}
      </div>
    </div>
  );
}
