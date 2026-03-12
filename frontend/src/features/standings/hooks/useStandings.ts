import { useQuery } from "@tanstack/react-query";
import { getSeasonStandings, getCurrentStandings } from "../api/standingsApi";
import type { TeamStanding } from "../types/standings";

export const useStandingsBySeason = (season?: string) => {
  return useQuery<TeamStanding[]>({
    queryKey: ["standings", season || "current"],
    queryFn: () => {
      //drugaciji endpoint za current i season
      if (!season || season.toLowerCase() === "current") {
        return getCurrentStandings();
      }
      return getSeasonStandings(season);
    },
    staleTime: 1000 * 60 * 10,
  });
};
