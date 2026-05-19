import { Outlet } from "react-router-dom";
import backgroundImage from "@/assets/images/stadium.webp";
import topScorerImage from "@/assets/images/haaland-celebration.webp";

export default function TopScorersLayout() {
  return (
    <>
      <div
        className="fixed inset-0 bg-center bg-cover"
        style={{ backgroundImage: `url(${backgroundImage})` }}
      />
      <div
        className="fixed inset-0 bg-center bg-cover opacity-50"
        style={{ backgroundImage: `url(${topScorerImage})` }}
      />
      <div className="fixed inset-0 bg-linear-to-b from-purple-600/30 via-purple-600/70 to-purple-600/90" />
      <Outlet />
    </>
  );
}
