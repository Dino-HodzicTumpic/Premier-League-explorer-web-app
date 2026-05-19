import axios from "axios";
import type { MatchDetails, MatchList } from "../types/matches";

export const getCurrentMatchWeek = async (): Promise<number> => {
  const { data } = await axios.get(
    `${import.meta.env.VITE_API_URL}/matches/current-gameweek`,
  );
  return data;
};

export const getMatchesByGameweek = async (
  gameweek: number,
  season?: number,
): Promise<MatchList[]> => {
  const { data } = await axios.get(
    `${import.meta.env.VITE_API_URL}/matches?gameweek=${gameweek}${season ? `&season=${season}` : ""}`,
  );
  return data;
};

export const getMatchDetails = async (
  espnMatchId: string,
): Promise<MatchDetails> => {
  const { data } = await axios.get(
    `${import.meta.env.VITE_API_URL}/matches/${espnMatchId}/details`,
  );
  return data;
};
