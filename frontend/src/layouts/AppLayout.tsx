import Navbar from "@/components/Navbar";
import { Routes, Route, useLocation } from "react-router-dom";
import StandingsPage from "@/pages/StandingsPage";
import { SkeletonTable } from "@/features/standings/components/SkeletonTable";
import backgroundImage from "@/assets/images/stadium.webp";

// Routes that show the stadium background
const STADIUM_ROUTES = ["/", "/standings"];

export default function AppLayout() {
  const { pathname } = useLocation();
  const showStadium = STADIUM_ROUTES.includes(pathname);

  return (
    <div className="relative min-h-screen  ">
      <div className="fixed inset-0 bg-linear-to-b from-purple-600/30 via-purple-600/70 to-purple-600/90" />

      {/* All visible content above the overlay */}
      <div className="relative z-10">
        <Navbar />
        <main className="flex justify-center items-center mt-6">
          <Routes>
            <Route path="/" element={<StandingsPage />} />
            <Route path="/standings" element={<StandingsPage />} />
            <Route path="/test" element={<SkeletonTable rows={20} />} />
          </Routes>
        </main>
      </div>
    </div>
  );
}
