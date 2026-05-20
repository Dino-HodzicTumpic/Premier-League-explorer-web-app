import { Routes, Route } from "react-router-dom";
import StadiumLayout from "./layouts/StadiumLayout";
import StandingsPage from "@/pages/StandingsPage";
import ScorersPage from "@/pages/ScorersPage";
import MatchesPage from "@/pages/MatchesPage";
import PurpleOverlayLayout from "./layouts/PurpleOverlayLayout";
import RootLayout from "./layouts/RootLayout";
import TopScorersLayout from "./layouts/TopScorersLayout";
import WaveOverlay from "./layouts/WaveOverlay";
import WaveOverlayGreen2 from "./layouts/WaveOverlayGreen2";
import MatchDetailsPage from "./pages/MatchDetailsPage";
import GrayOverlay from "./layouts/GrayOverlay";
import NewsLayout from "./layouts/NewsLayout";
import NewsDetailsPage from "./pages/NewsDetailsPage";
import NewsPage from "./pages/NewsPage";

export default function AppRouter() {
  return (
    <Routes>
      <Route element={<RootLayout />}>
        <Route element={<StadiumLayout />}>
          <Route path="/" element={<StandingsPage />} />
          <Route path="/standings" element={<StandingsPage />} />
        </Route>
        <Route element={<TopScorersLayout />}>
          <Route path="/top-scorers" element={<ScorersPage />} />
        </Route>
        <Route element={<WaveOverlayGreen2 />}>
          <Route path="/matches" element={<MatchesPage />} />
        </Route>

        <Route element={<GrayOverlay />}>
          <Route
            path="/matches/:espnMatchId/details"
            element={<MatchDetailsPage />}
          />
        </Route>

        <Route element={<NewsLayout />}>
          <Route path="/news" element={<NewsPage />} />
          <Route path="/news/:id" element={<NewsDetailsPage />} />
        </Route>
      </Route>
    </Routes>
  );
}
