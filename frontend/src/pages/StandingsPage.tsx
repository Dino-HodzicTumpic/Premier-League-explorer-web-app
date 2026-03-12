import { useEffect, useState } from "react";
import StandingsTable from "../features/standings/components/StandingsTable";
import { useMediaQuery } from "../hooks/useMediaQuery";
import { useStandingsBySeason } from "@/features/standings/hooks/useStandings";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";

const seasons = ["current", "2024/2025", "2023/2024"];

export default function StandingsPage() {
  const isMobile = useMediaQuery("(max-width: 640px)");
  const isTablet = useMediaQuery("(max-width: 1024px)");

  const [selectedSeason, setSelectedSeason] = useState(seasons[0]);

  const {
    data: standings,
    isLoading,
    isError,
  } = useStandingsBySeason(selectedSeason);

  return (
    <div className="space-y-6">
      <Select value={selectedSeason} onValueChange={setSelectedSeason}>
        <SelectTrigger>
          <SelectValue placeholder="Select a season" />
        </SelectTrigger>
        <SelectContent>
          {seasons.map((season) => (
            <SelectItem key={season} value={season}>
              {season}
            </SelectItem>
          ))}
        </SelectContent>
      </Select>

      <StandingsTable
        limit={20}
        isMini={isMobile}
        showExtendedStats={!isTablet}
        standings={standings}
        isLoading={isLoading}
        isError={isError}
      />
    </div>
  );
}
