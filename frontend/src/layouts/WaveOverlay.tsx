import React from "react";
import { Outlet } from "react-router-dom";

export default function WaveOverlay() {
  return (
    <>
      <div className="fixed inset-0 bg-[linear-gradient(135deg,#5B17C4_0%,#5B17C499_60%)] text-[#00FFAA]"></div>
      <Outlet />
    </>
  );
}
