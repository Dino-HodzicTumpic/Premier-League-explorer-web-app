import { Outlet } from "react-router-dom";
import Navbar from "@/components/Navbar";

export default function RootLayout() {
  return (
    <div className="relative min-h-screen">
      <div className="relative z-10">
        <Navbar />
        <main className="flex justify-center items-center mt-6">
          <Outlet />
        </main>
      </div>
    </div>
  );
}
