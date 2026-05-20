import React from "react";
import { VscJersey } from "react-icons/vsc";
import { getShortPlayerName } from "@/utils/getPlayerSurname";

const POSITION_MAP = {
  Goalkeeper: { bottom: "0%", left: "50%" },
  "Left Back": { bottom: "15%", left: "20%" },
  "Center Left Defender": { bottom: "15%", left: "40%" },
  "Center Right Defender": { bottom: "15%", left: "60%" },
  "Right Back": { bottom: "15%", left: "80%" },
  "Defensive Midfielder": { bottom: "40%", left: "50%" },
  "Center Left Midfielder": { bottom: "40%", left: "35%" },
  "Center Right Midfielder": { bottom: "40%", left: "65%" },
  "Attacking Midfielder Left": { bottom: "60%", left: "25%" },
  "Attacking Midfielder": { bottom: "60%", left: "50%" },
  "Attacking Midfielder Right": { bottom: "60%", left: "75%" },
  "Left Midfielder": { bottom: "40%", left: "35%" },
  "Right Midfielder": { bottom: "40%", left: "65%" },
  Forward: { bottom: "75%", left: "50%" },
  "Left Forward": { bottom: "80%", left: "30%" },
  "Right Forward": { bottom: "80%", left: "70%" },
};

type PlayerProps = {
  name: string;
  position: string;
  shirtNumber: number;
  isHomeTeam: boolean;
};

export default function Player({
  name,
  position,
  shirtNumber,
  isHomeTeam,
}: PlayerProps) {
  const cords = POSITION_MAP[position as keyof typeof POSITION_MAP] || {
    bottom: "50%",
    left: "50%",
  };

  return (
    <div
      className={` absolute -translate-x-1/2 -translate-y-1/2 text-center group ${isHomeTeam ? "rotate-180" : ""}`}
      style={{
        bottom: cords.bottom,
        left: cords.left,
      }}
    >
      <div className="flex flex-col justify-center items-center">
        <div className="relative w-10 h-10  ">
          <VscJersey className="w-full h-full text-white " />

          <span className="absolute inset-0 flex items-center justify-center text-xs font-bold text-black">
            {shirtNumber}
          </span>
        </div>

        <div>{getShortPlayerName(name)}</div>
      </div>
    </div>
  );
}
