import { useQuery } from "@tanstack/react-query";
import { getCurrentMatchWeek } from "../api/matchesApi";

export const useCurrentMatchWeek = () => {
  return useQuery<number>({
    queryKey: ["currentMatchWeek"],
    queryFn: () => getCurrentMatchWeek(),
    staleTime: 1000 * 60 * 60, // 1 hour
  });
};
