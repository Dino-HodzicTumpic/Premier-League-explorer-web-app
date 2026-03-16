import axios from "axios";
import type { Scorer } from "../types/scorers";

// API call to fetch top scorers for a specific season
export const getTopScorersForSeason = async (
  season: string,
  limit: number = 50,
): Promise<Scorer[]> => {
  const { data } = await axios.get(
    `${import.meta.env.VITE_API_URL}/top-scorers`,
    { params: { season: season, limit } },
  );
  console.log("Fetched top scorers data:", data);
  return data;
};
