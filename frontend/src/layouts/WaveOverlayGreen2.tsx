import React from "react";
import { Outlet } from "react-router-dom";

export default function WaveOverlay() {
  return (
    <>
      <div className="fixed inset-0 bg-[#0D1B2A] bg-[linear-gradient(135deg,#0D1B2A_0%,#0D1B2A99_60%)] text-[#00FFAA] accent-[#00FFAA]"></div>
      <Outlet />
    </>
  );
}
