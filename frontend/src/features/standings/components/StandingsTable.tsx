import type { TeamStanding } from "../types/standings";
import { SkeletonTable } from "./SkeletonTable";
import {
  Table,
  TableBody,
  TableCaption,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { cn } from "@/lib/utils";

type StandingsTableProps = {
  limit: number;
  isMini: boolean;
  showExtendedStats: boolean;
  standings?: TeamStanding[];
  isLoading?: boolean;
  isError?: boolean;
};

const tableHeadersExtended = [
  "Pos",
  "Team",
  "PL",
  "W",
  "D",
  "L",
  "DIFF",
  "GLS",
  "PTS",
];
const tableHeadersMini = ["#", "Team", "P", "DIFF", "PTS"];

export default function StandingsTable({
  limit,
  isMini,
  showExtendedStats,
  standings,
  isLoading,
  isError,
}: StandingsTableProps) {
  if (isLoading) {
    return <SkeletonTable rows={limit} />;
  }
  if (isError) {
    return <div>Error loading standings</div>;
  }

  const displayedStandings = standings?.slice(0, limit) || [];
  const headers = showExtendedStats ? tableHeadersExtended : tableHeadersMini;
  const compactCell = isMini && "px-2 py-1";

  return (
    <div
      className={cn(
        "bg-white/10 mb-10 backdrop-blur-md rounded-2xl border border-white/20 shadow-lg overflow-hidden",
        isMini && "text-sm",
      )}
    >
      <Table>
        {!isMini && (
          <TableCaption className="text-bold">
            Premier League Standings
          </TableCaption>
        )}
        <TableHeader>
          <TableRow className="bg-muted/60  hover:bg-muted/60">
            {headers.map((header) => (
              <TableHead
                key={header}
                className={cn(
                  "text-xs uppercase tracking-wider text-muted-foreground",
                  compactCell,
                )}
              >
                {header}
              </TableHead>
            ))}
          </TableRow>
        </TableHeader>
        <TableBody className="[&_tr:nth-child(even)]:bg-muted/30 [&_tr:nth-child(even)]:hover:bg-muted/50">
          {displayedStandings.map((team) => (
            <TableRow key={team.externalId}>
              <TableCell className={cn("text-center ", compactCell)}>
                {team.position}
              </TableCell>
              <TableCell className={cn("flex items-center gap-2", compactCell)}>
                <img
                  src={team.crestUrl}
                  alt={team.name}
                  className={cn(
                    "object-contain",
                    isMini ? "h-4 w-4" : "h-6 w-6",
                  )}
                />
                <span className="font-medium">
                  {isMini ? team.tla : team.name}
                </span>
              </TableCell>
              <TableCell className={cn("text-center", compactCell)}>
                {team.playedGames}
              </TableCell>
              {showExtendedStats && (
                <>
                  <TableCell className={cn("text-center", compactCell)}>
                    {team.won}
                  </TableCell>
                  <TableCell className={cn("text-center", compactCell)}>
                    {team.draw}
                  </TableCell>
                  <TableCell className={cn("text-center", compactCell)}>
                    {team.lost}
                  </TableCell>
                </>
              )}
              <TableCell className={cn("text-center", compactCell)}>
                {team.goalDifference}
              </TableCell>
              {showExtendedStats && (
                <TableCell className={cn("text-center", compactCell)}>
                  {team.goalsFor}:{team.goalsAgainst}
                </TableCell>
              )}
              <TableCell
                className={cn(
                  "text-center font-bold text-primary",
                  compactCell,
                )}
              >
                {team.points}
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </div>
  );
}
