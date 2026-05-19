import { useQuery } from "@tanstack/react-query";
import { getMatchDetails } from "../api/matchesApi";

export const useMatchDetails = (espnMatchId: string) => {
  return useQuery({
    queryKey: ["matchDetails", espnMatchId],
    queryFn: () => getMatchDetails(espnMatchId),
    enabled: !!espnMatchId,
    staleTime: 1000 * 15, // 15 seconds
    refetchInterval: 1000 * 15, // 15 seconds
  });
};
