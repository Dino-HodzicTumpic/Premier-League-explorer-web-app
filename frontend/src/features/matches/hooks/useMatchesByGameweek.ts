import { useQuery } from "@tanstack/react-query";
import type { Match } from "date-fns";
import { getMatchesByGameweek } from "../api/matchesApi";

export const useMatchesByGameweek = (gameweek: number, season?: number) => {
  return useQuery({
    queryKey: ["matches", { gameweek, season }],
    queryFn: () => getMatchesByGameweek(gameweek, season),
    staleTime: 1000 * 15, // 15 seconds
    refetchInterval: 1000 * 15, // 15 seconds
  });
};
