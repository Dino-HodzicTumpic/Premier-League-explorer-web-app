import { Outlet } from "react-router-dom";

export default function PurpleOverlayLayout() {
  return (
    <>
      <div className="fixed inset-0 bg-linear-to-b from-purple-600/30 via-purple-600/70 to-purple-600/90" />
      <Outlet />
    </>
  );
}
