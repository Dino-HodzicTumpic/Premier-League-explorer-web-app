import { Outlet } from "react-router-dom";
import backgroundImage from "@/assets/images/stadium.webp";

export default function StadiumLayout() {
  return (
    <>
      <div
        className="fixed inset-0 bg-center bg-cover"
        style={{ backgroundImage: `url(${backgroundImage})` }}
      />
      <div className="fixed inset-0 bg-linear-to-b from-purple-600/30 via-purple-600/70 to-purple-600/90" />
      <Outlet />
    </>
  );
}
