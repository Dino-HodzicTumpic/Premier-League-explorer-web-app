import NewsArticle from "@/features/news/components/NewsArticle";
import { useLatestNews } from "@/features/news/hooks/useLatestNews";
import React, { useEffect } from "react";

export default function NewsPage() {
  const { data: latestNews, isLoading, isError, error } = useLatestNews();

  useEffect(() => {
    console.log("Latest news data:", latestNews);
  }, [latestNews]);

  if (isLoading) {
    return <div className="text-center">Loading...</div>;
  }

  if (isError) {
    return (
      <div className="text-center">
        Error: {error instanceof Error ? error.message : "Unknown error"}
      </div>
    );
  }

  return (
    <div className="flex flex-col gap-8 p-4">
      <div className="">
        <h4 className="text-purple-500 font-bold text-3xl">Latest</h4>
        <h2 className="text-white font-bold text-5xl">Premier League News</h2>
      </div>
      <div className="flex flex-col gap-8 text-white">
        {latestNews?.news.map((n) => (
          <NewsArticle key={n.id} {...n} />
        ))}
      </div>
    </div>
  );
}
