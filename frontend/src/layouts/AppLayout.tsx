import Navbar from "@/components/Navbar";
import { Routes, Route, useLocation } from "react-router-dom";
import StandingsPage from "@/pages/StandingsPage";
import { SkeletonTable } from "@/features/standings/components/SkeletonTable";
import backgroundImage from "@/assets/images/stadium.webp";
import topScorerImage from "@/assets/images/haaland-celebration.webp";
import ScorersPage from "@/pages/ScorersPage";
import { SkeletonScorers } from "@/features/topScorers/components/SkeletonScorers";
import MatchesPage from "@/pages/MatchesPage";

// Routes that show the stadium background
const STADIUM_ROUTES = ["/", "/top-scorers"];

export default function AppLayout() {
  const { pathname } = useLocation();
  const showStadium = STADIUM_ROUTES.includes(pathname);

  return (
    <div className="relative min-h-screen  ">
      {showStadium && (
        <div
          className="fixed inset-0 bg-center bg-cover"
          style={{
            backgroundImage: `url(${backgroundImage})`,
          }}
        />
      )}
      {pathname === "/top-scorers" && topScorerImage && (
        <div
          className="fixed inset-0 bg-center bg-cover opacity-50"
          style={{ backgroundImage: `url(${topScorerImage})` }}
        />
      )}
      <div className="fixed inset-0 bg-linear-to-b from-purple-600/30 via-purple-600/70 to-purple-600/90" />
      {/* All visible content above the overlay */}
      <div className="relative z-10">
        <Navbar />
        <main className="flex justify-center items-center mt-6">
          <Routes>
            <Route path="/" element={<StandingsPage />} />
            <Route path="/standings" element={<StandingsPage />} />
            <Route path="/test" element={<SkeletonScorers rows={20} />} />
            <Route path="/top-scorers" element={<ScorersPage />} />
            <Route path="/matches" element={<MatchesPage />} />
          </Routes>
        </main>
      </div>
    </div>
  );
}
