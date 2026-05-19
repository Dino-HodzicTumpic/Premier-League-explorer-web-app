import type { Match } from "date-fns";

export interface MatchList {
  matchId: number;
  espnMatchId: string;
  injuryTime: number | null;
  minute?: number;
  status: MatchStatus;
  homeScore: number;
  awayScore: number;
  winner: MatchResult | null;
  homeTeam: Team;
  awayTeam: Team;
  kickoffTime: string;
}

export type MatchStatus =
  | "SCHEDULED"
  | "LIVE"
  | "FINISHED"
  | "IN_PLAY"
  | "PAUSED"
  | "POSTPONED"
  | "SUSPENDED"
  | "CANCELLED";
export type MatchResult = "HOME_TEAM" | "AWAY_TEAM" | "DRAW";

export type Team = {
  id: number;
  name: string;
  shortName: string;
  tla: string;
  crestUrl: string;
};

export interface MatchDetails {
  espnMatchId: string;
  minute: number | null;
  status: MatchStatus;

  injuryTime: number | null;
  utcDate: string; // LocalDateTime → ISO string
  matchday: number;

  winner: MatchWinner | null;

  stadium: string;
  attendance: number | null;

  matchEvents: MatchEvent[];
  referees: MatchReferee[];

  homeTeam: TeamDetails;
  awayTeam: TeamDetails;
}

export type MatchWinner = "HOME_TEAM" | "AWAY_TEAM" | "DRAW";

export interface MatchReferee {
  id: number;
  name: string;
  nationality: string;
}

export interface EventPlayer {
  role: PlayerRole;
  playerId: string;
  name: string;
}

export type PlayerRole = "SCORER" | "ASSIST" | "IN" | "OUT" | "BOOKED";

export interface TeamDetails {
  espnTeamId: string;
  tla: string;
  shortName: string;
  crestUrl: string;
  homeAway: string;
  formation: string;

  stats: TeamStats;

  score: number;

  appearances: MatchAppearance[];
}

export interface MatchAppearance {
  playerName: string;
  playerId: number;
  shirtNumber: number;
  position: string;
  starting: boolean;
}

export interface TeamStats {
  cornerKicks: number | null;
  goalKicks: number | null;
  offsides: number | null;
  fouls: number | null;
  ballPossession: number | null;
  accuratePasses: number | null;
  totalPasses: number | null;
  saves: number | null;
  throwIns: number | null;
  shots: number | null;
  shotsOnGoal: number | null;
  shotsOffGoal: number | null;
  yellowCards: number | null;
  redCards: number | null;
  totalBookings: number | null;
}

export interface MatchEvent {
  type: string;
  minute: number;

  team: Team;

  players: EventPlayer[];

  card: "YELLOW" | "RED" | null;
}
