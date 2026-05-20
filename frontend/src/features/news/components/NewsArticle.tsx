import React from "react";
import { formatDate } from "@/utils/formatDate";
import { useNavigate } from "react-router-dom";

type NewsArticleProps = {
  id: number;
  headline: string;
  thumbnailUrl: string;
  dateCreated: string;
};

export default function NewsArticle({
  id,
  headline,
  thumbnailUrl,
  dateCreated,
}: NewsArticleProps) {
  const navigate = useNavigate();

  return (
    <div
      className=" flex flex-col md:flex-row justify-center  cursor-pointer "
      onClick={() => navigate(`/news/${id}`)}
    >
      <div className=" md:w-1/4 ">
        <img src={thumbnailUrl} alt="thumbnail" className="rounded-lg" />
      </div>
      <div className="flex flex-col md:w-2/4 justify-between hover:bg-[#3f0a4d] bg-[#3f0a4d]/70 rounded-lg">
        <div className="flex w-full ml-6 mt-4 pr-6">
          <span className="text-xl whitespace-normal break-words">
            {headline}
          </span>
        </div>
        <div className="flex justify-start ml-6 mb-5 font-light">
          <span>{formatDate(dateCreated)}</span>
        </div>
      </div>
    </div>
  );
}
