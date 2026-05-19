import React from "react";

type MatchStatProps = {
  stateName: string;
  homeTeamValue: number | null;
  awayTeamValue: number | null;
};

export default function MatchStat({
  stateName,
  homeTeamValue,
  awayTeamValue,
}: MatchStatProps) {
  const leftValue = homeTeamValue ?? 0;
  const rightValue = awayTeamValue ?? 0;
  const total = leftValue + rightValue;
  const homePercent = total === 0 ? 0 : (leftValue / total) * 100;
  const awayPercent = total === 0 ? 0 : (rightValue / total) * 100;

  return (
    <div>
      {/* Header */}
      <div className="flex justify-between items-center">
        <span>{leftValue}</span>
        <span>{stateName}</span>
        <span>{rightValue}</span>
      </div>

      {/* Bars */}
      <div className="flex justify-between items-center gap-2 md:gap-8 ">
        {/* Left side */}
        <div className="flex flex-1 justify-start h-2 bg-gray-200 rounded-full">
          <div
            className="h-full bg-green-500 rounded-full"
            style={{ width: `${homePercent}%` }}
          />
        </div>

        {/* Right side */}
        <div className="flex flex-1 justify-end h-2 bg-gray-200 rounded-full">
          <div
            className="h-full bg-blue-500 rounded-full"
            style={{ width: `${awayPercent}%` }}
          />
        </div>
      </div>
    </div>
  );
}
