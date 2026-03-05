import "./App.css";
import { useState } from "react";
import Navbar2, { type NavUser } from "@/components/Navbar2";
import Navbar from "@/components/Navbar";
import { BrowserRouter, Routes } from "react-router-dom";
// Mock user — set to `null` to preview the logged-out state
const MOCK_USER: NavUser = {
  name: "John Doe",
  avatarUrl: undefined,
};

function App() {
  const [user, setUser] = useState<NavUser | null>(MOCK_USER);

  return (
    <BrowserRouter>
      <div className="min-h-screen ">
        <Navbar />
        <main className="">
          {/* Page content goes here */}

          <Routes></Routes>
        </main>
      </div>
    </BrowserRouter>
  );
}

export default App;
