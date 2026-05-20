import axios from "axios";
import type { News, NewsArticle } from "../types/news";

export const fetchLatestNews = async (): Promise<News> => {
  const { data } = await axios.get(
    `${import.meta.env.VITE_API_URL}/news/latest`,
  );
  return data;
};
