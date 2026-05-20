export type NewsArticle = {
  id: number;
  headline: string;
  body: string;
  thumbnailUrl: string;
  dateCreated: string;
};

export type News = {
  news: NewsArticle[];
};
