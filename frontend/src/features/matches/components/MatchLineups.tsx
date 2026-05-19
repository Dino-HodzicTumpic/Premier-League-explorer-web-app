import type { Match } from "date-fns";
import React, { useEffect } from "react";
import type { MatchDetails } from "../types/matches";
import TeamPitch from "./TeamPitch";
import { Spinner } from "@/components/Spinner";
import { SpinnerCustom } from "@/components/SpinnerCustom";

type MatchLineupsProps = {
  matchDetails: MatchDetails;
};

export default function MatchLineups({ matchDetails }: MatchLineupsProps) {
  if (!matchDetails) {
    return <SpinnerCustom />;
  }

  const homeStarters = matchDetails.homeTeam.appearances.filter(
    (app) => app.starting,
  );
  const awayStarters = matchDetails.awayTeam.appearances.filter(
    (app) => app.starting,
  );

  useEffect(() => {
    console.log("details:", matchDetails);
  }, []);

  return (
    <div className="">
      <TeamPitch
        starters={homeStarters}
        teamName={matchDetails.homeTeam.shortName}
        isHomeTeam={true}
      />
      <TeamPitch
        starters={awayStarters}
        teamName={matchDetails.awayTeam.shortName}
        isHomeTeam={false}
      />
    </div>
  );
}
