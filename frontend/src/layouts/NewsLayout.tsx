import { Outlet } from "react-router-dom";
import logoPl2 from "../assets/images/logoPl2.webp";

export default function NewsLayout() {
  return (
    <>
      {/* Dark navy/purple solid background */}
      <div className="fixed inset-0 z-0 bg-[#350441]" />

      {/* Subtle radial glow top-left */}
      <div className="fixed inset-0 z-0 bg-[radial-gradient(ellipse_60%_50%_at_0%_0%,rgba(88,28,135,0.35),transparent)]" />

      {/* Faint PL lion watermark top-right */}
      <div
        className="fixed top-18 right-24 z-0 w-72 h-72 opacity-[0.25] bg-no-repeat bg-contain bg-top-right pointer-events-none"
        style={{
          backgroundImage: `url(${logoPl2})`,
        }}
      />

      <div className="relative z-10">
        <Outlet />
      </div>
    </>
  );
}
