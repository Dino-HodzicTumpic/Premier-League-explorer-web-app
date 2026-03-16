import { Skeleton } from "@/components/ui/skeleton";

type SkeletonScorersProps = {
  rows?: number;
};

export function SkeletonScorers({ rows = 10 }: SkeletonScorersProps) {
  return (
    <div className="flex w-full max-w-sm md:max-w-md flex-col gap-2 my-8">
      {Array.from({ length: rows }).map((_, index) => (
        <div className="flex items-center gap-4" key={index}>
          <Skeleton className="h-4 w-8" />
          <Skeleton className="h-10 w-10 rounded-full" />
          <Skeleton className="h-4 flex-1" />
          <Skeleton className="h-4 w-20" />
        </div>
      ))}
    </div>
  );
}
