import { Button } from "@/components/ui/button";
import { useLatestNews } from "@/features/news/hooks/useLatestNews";
import React from "react";
import { useNavigate, useParams } from "react-router-dom";
import { formatDate } from "@/utils/formatDate";

export default function NewsDetailsPage() {
  const { id } = useParams();
  const { data: articles, isLoading, isError, error } = useLatestNews();
  const navigate = useNavigate();

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

  if (!id) {
    return <div className="text-center">Article not found</div>;
  }

  const article = articles?.news.find((a) => a.id === parseInt(id, 10));

  if (!article) {
    return <div className="text-center">Article not found</div>;
  }

  return (
    <div className="flex flex-col md:w-3/4 gap-4 p-4 text-white mx-auto">
      <div className="flex justify-start">
        <Button
          variant="default"
          className="mb-4 text-purple-400 hover:text-purple-700 cursor-pointer"
          onClick={() => navigate("/news")}
        >
          &lt; Back to News
        </Button>
      </div>
      <div>
        <img
          src={article.thumbnailUrl}
          alt="thumbnail"
          className="rounded-lg"
        />
      </div>
      <div className="flex flex-col gap-8 bg-[#350441]/70 p-6 rounded-lg">
        <span className="font-light">{formatDate(article.dateCreated)}</span>
        <h1 className=" text-xl md:text-3xl font-bold">{article.headline}</h1>
        <div className="space-y-4">
          {article.body.split(/\n\s*\n/).map((paragraph, idx) => (
            <p key={idx} className="whitespace-pre-line">
              {paragraph.split(/\*\*(.+?)\*\*/g).map((segment, segmentIdx) =>
                segmentIdx % 2 === 1 ? (
                  <span key={segmentIdx} className="font-semibold text-lg">
                    {segment}
                  </span>
                ) : (
                  <span key={segmentIdx}>{segment}</span>
                ),
              )}
            </p>
          ))}
        </div>
      </div>
    </div>
  );
}
