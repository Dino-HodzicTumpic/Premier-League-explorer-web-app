import StandingsTable from "../features/standings/components/StandingsTable";
import { useMediaQuery } from "../hooks/useMediaQuery";

export default function StandingsPage() {
  const isMobile = useMediaQuery("(max-width: 640px)");
  const isTablet = useMediaQuery("(max-width: 1024px)");

  return (
    <div className="">
      <StandingsTable
        limit={20}
        isMini={isMobile}
        showExtendedStats={!isTablet}
      />
    </div>
  );
}
