import React from "react";
import type { MatchEvent } from "../types/matches";
import EventSub from "./EventSub";
import EventBooking from "./EventBooking";
import EventGoal from "./EventGoal";

type MatchOverviewProps = {
  matchEvents: MatchEvent[];
  homeTeamTla: string;
};

export default function MatchOverview({
  matchEvents,
  homeTeamTla,
}: MatchOverviewProps) {
  return (
    <div className="flex flex-col gap-4 mx-2 mb-10 bg-white rounded-md p-4">
      {matchEvents.reverse().map((event, index) => {
        switch (event.type) {
          case "GOAL":
          case "OWN_GOAL":
          case "PENALTY_GOAL":
            return (
              <EventGoal
                key={index}
                type={event.type}
                team={event.team}
                players={event.players}
                minute={event.minute}
                homeTeamTla={homeTeamTla}
              />
            );
          case "BOOKING":
            return (
              <EventBooking
                key={index}
                team={event.team}
                players={event.players}
                minute={event.minute}
                card={event.card}
                homeTeamTla={homeTeamTla}
              />
            );
          case "SUBSTITUTION":
            return (
              <EventSub
                key={index}
                team={event.team}
                players={event.players}
                minute={event.minute}
                homeTeamTla={homeTeamTla}
              />
            );
          default:
            return null;
        }
      })}
    </div>
  );
}
