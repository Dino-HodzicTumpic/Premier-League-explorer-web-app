import axios from "axios";
import type { TeamStanding } from "../types/standings";

export const getStandings = async (): Promise<TeamStanding[]> => {
  const { data } = await axios.get(`${import.meta.env.VITE_API_URL}/standings`);
  return data;
};
