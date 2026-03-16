import React, { use, useEffect } from "react";
import type { Scorer } from "../types/scorers";
import {
  Table,
  TableBody,
  TableCaption,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { SkeletonScorers } from "./SkeletonScorers";
import { cn } from "@/lib/utils";

type TopScorersProps = {
  scorersCount?: number; // how many top scorers to show
  isMini?: boolean; // different styling for mini version (e.g. for homePage)
  scorers?: Scorer[];
  isLoading?: boolean;
  isError?: boolean;
};

export default function TopScorers({
  scorersCount = 10,
  isMini,
  scorers,
  isLoading,
  isError,
}: TopScorersProps) {
  if (isLoading) {
    return <SkeletonScorers rows={scorersCount} />;
  }

  if (isError) {
    return <div>Error loading top scorers</div>;
  }

  const displayedScorers = scorers?.slice(0, scorersCount) || [];
  const topScorerImage = displayedScorers[0]?.playerImageUrl;

  return (
    <div
      className={cn(
        " bg-white/30 mb-10 backdrop-blur-md rounded-2xl border border-white/20 shadow-xl overflow-hidden p-4 bg-center bg-cover",
        isMini && "text-sm",
      )}
    >
      <Table className="">
        <TableCaption className="text-black font-semibold text-md">
          Top {scorersCount} Scorers
        </TableCaption>
        <TableHeader>
          <TableRow>
            <TableHead>Rank</TableHead>
            <TableHead>Player</TableHead>
            <TableHead>Goals</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {displayedScorers.map((scorer, index) => (
            <TableRow key={scorer.externalId}>
              <TableCell>{index + 1}</TableCell>
              <TableCell>
                <div className="flex md:w-2xl space-x-2">
                  <img
                    src={scorer.playerImageUrl}
                    alt=""
                    className="w-16 h-16 md:w-24 md:h-24 "
                  />
                  <div className="flex flex-col items-start space-y-2 ">
                    <span className="font-semibold"> {scorer.playerName}</span>
                    <span className="flex items-center  text-gray-800">
                      <img
                        src={scorer.teamCrestUrl}
                        alt=""
                        className="w-6 h-6"
                      />
                      {scorer.teamShortName}
                    </span>
                  </div>
                </div>
              </TableCell>
              <TableCell className="font-bold">
                {scorer.numberOfGoals}
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </div>
  );
}
