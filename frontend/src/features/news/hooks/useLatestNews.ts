import { useQuery } from "@tanstack/react-query";
import { fetchLatestNews } from "../api/newsApi";

export const useLatestNews = () => {
  return useQuery({
    queryKey: ["latestNews"],
    queryFn: () => fetchLatestNews(),
    staleTime: 1000 * 60 * 60 * 2, // 2 hours
  });
};
