import React from "react";
import type { TeamStats } from "../types/matches";
import MatchStat from "./MatchStat";

type MatchStatsProps = {
  homeTeamStats: TeamStats;
  awayTeamStats: TeamStats;
};

const labels: Record<keyof TeamStats, string> = {
  cornerKicks: "Corner kicks",
  goalKicks: "Goal kicks",
  offsides: "Offsides",
  fouls: "Fouls",
  ballPossession: "Ball possession",
  accuratePasses: "Accurate passes",
  totalPasses: "Total passes",
  saves: "Saves",
  throwIns: "Throw-ins",
  shots: "Shots",
  shotsOnGoal: "Shots on goal",
  shotsOffGoal: "Shots off goal",
  yellowCards: "Yellow cards",
  redCards: "Red cards",
  totalBookings: "Total bookings",
};

export default function MatchStats({
  homeTeamStats,
  awayTeamStats,
}: MatchStatsProps) {
  const stats = (Object.keys(homeTeamStats) as (keyof TeamStats)[]).map(
    (key) => ({
      key,
      label: labels[key],
      home: homeTeamStats[key] ?? 0,
      away: awayTeamStats[key] ?? 0,
    }),
  );

  return (
    <div className="flex flex-col sm:mx-4 md:mx-0 gap-2">
      {stats.map((stat) => (
        <MatchStat
          stateName={stat.label || stat.key}
          homeTeamValue={stat.home}
          awayTeamValue={stat.away}
        />
      ))}
    </div>
  );
}
