import type { MatchStatus } from "@/features/matches/types/matches";

export const formatKickoff = (kickoffTime: string, status: MatchStatus) => {
  const kickOff = new Date(kickoffTime);
  const now = new Date();

  const isToday = kickOff.toDateString() === now.toDateString();

  const time = kickOff.toLocaleTimeString([], {
    hour: "2-digit",
    minute: "2-digit",
  });

  const dateOnly = kickOff.toLocaleDateString([], {
    day: "2-digit",
    month: "2-digit",
    year: "2-digit",
  });

  if (status === "LIVE" || status === "IN_PLAY") return time;

  if (isToday) return time;

  return dateOnly;
};
