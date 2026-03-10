import { useQuery } from "@tanstack/react-query";
import { getStandings } from "../api/standingsApi";

export const useStandings = () => {
  return useQuery({
    queryKey: ["standings"],
    queryFn: getStandings,
    staleTime: 1000 * 60 * 10, // 10 minuta
  });
};
