import axios from "axios";
import type { TeamStanding } from "../types/standings";

// API call to fetch current standings
export const getCurrentStandings = async (): Promise<TeamStanding[]> => {
  const { data } = await axios.get(
    `${import.meta.env.VITE_API_URL}/standings/current`,
  );
  return data;
};

// API call to fetch standings for a specific season
export const getSeasonStandings = async (
  season: string,
): Promise<TeamStanding[]> => {
  const { data } = await axios.get(
    `${import.meta.env.VITE_API_URL}/standings`,
    {
      params: { season },
    },
  );
  return data;
};
