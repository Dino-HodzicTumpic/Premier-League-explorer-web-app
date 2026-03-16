import {
  Select,
  SelectValue,
  SelectTrigger,
  SelectContent,
  SelectItem,
} from "@/components/ui/select";
import { SkeletonScorers } from "@/features/topScorers/components/SkeletonScorers";
import TopScorers from "@/features/topScorers/components/TopScorers";
import { useTopScorers } from "@/features/topScorers/hooks/useTopScorers";
import React, { useState } from "react";

const seasons = [
  { name: "2025/2026", value: "2025" },
  { name: "2024/2025", value: "2024" },
  { name: "2023/2024", value: "2023" },
];

export default function ScorersPage() {
  const [selectedSeason, setSelectedSeason] = useState<string>(
    seasons[0].value,
  );

  const { data: scorers, isLoading, isError } = useTopScorers(selectedSeason);

  return (
    <div className="w-full flex flex-col justify-center items-center  space-y-6 ">
      <Select value={selectedSeason} onValueChange={setSelectedSeason}>
        <SelectTrigger className="bg-white">
          <SelectValue placeholder="Select a season" />
        </SelectTrigger>
        <SelectContent>
          {seasons.map((season) => (
            <SelectItem key={season.value} value={season.value}>
              {season.name}
            </SelectItem>
          ))}
        </SelectContent>
      </Select>

      <TopScorers
        scorersCount={20}
        isMini={false}
        scorers={scorers}
        isLoading={isLoading}
        isError={isError}
      />
    </div>
  );
}
