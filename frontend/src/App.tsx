import "./App.css";
import { BrowserRouter } from "react-router-dom";
import AppLayout from "@/layouts/AppLayout";
import AppRouter from "./AppRouter";

function App() {
  return (
    <BrowserRouter>
      <AppRouter />
    </BrowserRouter>
  );
}

export default App;
