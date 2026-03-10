export interface TeamStanding {
  externalId: number;
  name: string;
  shortName: string;
  tla: string;
  crestUrl: string;
  position: number;
  playedGames: number;
  form: string;
  won: number;
  draw: number;
  lost: number;
  points: number;
  goalsFor: number;
  goalsAgainst: number;
  goalDifference: number;
}
