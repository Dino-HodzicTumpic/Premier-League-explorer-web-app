import { useQuery } from "@tanstack/react-query";
import { getTopScorersForSeason } from "../api/scorersApi";
import type { Scorer } from "../types/scorers";

export const useTopScorers = (season: string, limit?: number) => {
  return useQuery<Scorer[]>({
    queryKey: ["top-scorers", season, limit],
    queryFn: () => getTopScorersForSeason(season, limit),
    staleTime: 1000 * 60 * 10, // 10 minutes
  });
};
