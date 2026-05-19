import React from "react";
import { Outlet } from "react-router-dom";

export default function GrayOverlay() {
  return (
    <>
      <div className="fixed inset-0 bg-gray-300/30 z-0" />
      <Outlet />
    </>
  );
}
