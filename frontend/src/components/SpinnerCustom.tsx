import { Spinner } from "./Spinner";

export function SpinnerCustom() {
  return (
    <div className="flex items-center justify-center gap-2 h-64">
      <Spinner />
      <span>Loading</span>
    </div>
  );
}
