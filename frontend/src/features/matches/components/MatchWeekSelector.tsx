import React, { useState } from "react";
import { useCurrentMatchWeek } from "../hooks/useCurrentMatchWeek";
import { ChevronLeft, ChevronRight, ChevronDown, Calendar } from "lucide-react";
import { Button } from "@/components/ui/button";
import {
  Popover,
  PopoverTrigger,
  PopoverContent,
  PopoverHeader,
  PopoverTitle,
  PopoverDescription,
} from "@/components/ui/popover";

type MatchWeekSelectorProps = {
  gameWeekSelected: number | null;
  setGameWeekSelected: React.Dispatch<React.SetStateAction<number>>;
};

export default function MatchWeekSelector({
  gameWeekSelected,
  setGameWeekSelected,
}: MatchWeekSelectorProps) {
  const handlePrev = () => {
    setGameWeekSelected((prev) => (prev && prev > 1 ? prev - 1 : prev));
  };

  const handleNext = () => {
    setGameWeekSelected((prev) => (prev && prev < 38 ? prev + 1 : prev));
  };

  const handleSelection = (gw: number) => {
    setGameWeekSelected(gw);
  };

  return (
    <div className="bg-white/10 backdrop-blur-md border border-white/20 rounded-xl mb-8 p-4">
      <div className="flex justify-center items-center">
        <Button variant="ghost" className="rounded-full" onClick={handlePrev}>
          <ChevronLeft />
        </Button>
        <Popover>
          <PopoverTrigger asChild>
            <Button variant="outline">
              <span>GW {gameWeekSelected}</span>
              <ChevronDown />
            </Button>
          </PopoverTrigger>
          <PopoverContent>
            <PopoverHeader>
              <PopoverTitle>GameWeek Selector</PopoverTitle>
            </PopoverHeader>
            <div className="mt-2 grid grid-cols-4 gap-1">
              {Array.from({ length: 38 }, (_, i) => i + 1).map((gw) => (
                <button
                  className="p-2 rounded hover:bg-gray-100"
                  key={gw}
                  onClick={() => handleSelection(gw)}
                >
                  GW{gw}
                </button>
              ))}
            </div>
          </PopoverContent>
        </Popover>
        <Button variant="ghost" className="rounded-full" onClick={handleNext}>
          <ChevronRight />
        </Button>
      </div>
    </div>
  );
}
