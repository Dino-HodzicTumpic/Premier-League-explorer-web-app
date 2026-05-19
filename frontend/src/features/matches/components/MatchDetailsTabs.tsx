import React from "react";
import type { MatchDetails, Team } from "../types/matches";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import MatchOverview from "./MatchOverview";
import MatchStats from "./MatchStats";
import MatchLineups from "./MatchLineups";
import type { Match } from "date-fns";

type MatchDetailsTabsProps = {
  matchDetails: MatchDetails;
};

export default function MatchDetailsTabs({
  matchDetails,
}: MatchDetailsTabsProps) {
  return (
    <Tabs>
      <TabsList className="flex justify-center w-full">
        <TabsTrigger value="details">Details</TabsTrigger>
        <TabsTrigger value="stats">Stats</TabsTrigger>
        <TabsTrigger value="lineup">Lineup</TabsTrigger>
      </TabsList>
      <TabsContent value="details">
        <MatchOverview
          matchEvents={matchDetails.matchEvents}
          homeTeamTla={matchDetails.homeTeam.tla}
        />
      </TabsContent>
      <TabsContent value="stats">
        <MatchStats
          homeTeamStats={matchDetails.homeTeam.stats}
          awayTeamStats={matchDetails.awayTeam.stats}
        />
      </TabsContent>
      <TabsContent value="lineup">
        <MatchLineups matchDetails={matchDetails} />
      </TabsContent>
    </Tabs>
  );
}
