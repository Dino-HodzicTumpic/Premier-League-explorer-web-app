import { useState } from "react";
import {
  LogIn,
  Menu,
  X,
  User,
  ListOrdered,
  Star,
  Users,
  LogOut,
} from "lucide-react";

import plLogo from "@/assets/images/plLogo.jpg";
import { Button } from "@/components/ui/button";
import { Avatar, AvatarImage, AvatarFallback } from "@/components/ui/avatar";
import {
  DropdownMenu,
  DropdownMenuTrigger,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuLabel,
} from "@/components/ui/dropdown-menu";
import {
  NavigationMenu,
  NavigationMenuList,
  NavigationMenuItem,
  NavigationMenuLink,
  navigationMenuTriggerStyle,
} from "@/components/ui/navigation-menu";
import { cn } from "@/lib/utils";

// ─── Types ────────────────────────────────────────────────────────────────────

export type NavUser = {
  name: string;
  avatarUrl?: string;
};

type NavbarProps = {
  user?: NavUser | null;
  onSignIn?: () => void;
  onSignOut?: () => void;
  onProfile?: () => void;
  onMyTeams?: () => void;
};

// ─── Nav links ────────────────────────────────────────────────────────────────

const NAV_LINKS = [
  { label: "Standings", href: "#standings", icon: ListOrdered },
  { label: "Top Scorers", href: "#top-scorers", icon: Star },
  { label: "Teams", href: "#teams", icon: Users },
  { label: "Players", href: "#players", icon: User },
] as const;

// ─── Component ────────────────────────────────────────────────────────────────

export default function Navbar2({
  user,
  onSignIn,
  onSignOut,
  onProfile,
  onMyTeams,
}: NavbarProps) {
  const [mobileOpen, setMobileOpen] = useState(false);

  /** Initials fallback for avatar */
  const initials = user?.name
    .split(" ")
    .map((n) => n[0])
    .join("")
    .slice(0, 2)
    .toUpperCase();

  return (
    <header className="sticky top-0 z-50 w-full border-b bg-background/95 backdrop-blur supports-backdrop-filter:bg-background/60">
      <div className="mx-auto flex h-16 max-w-7xl items-center justify-between px-4 sm:px-6">
        {/* ── Brand ───────────────────────────────────────────────────────── */}
        <a
          href="#"
          className="flex shrink-0 items-center gap-3 focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring rounded-sm"
          aria-label="Premier League Tracker home"
        >
          <img
            src={plLogo}
            alt="PL logo"
            className="h-9 w-9 rounded-full object-cover"
          />
          <span className="hidden text-sm font-semibold leading-tight sm:block">
            Premier League
            <br />
            <span className="text-muted-foreground font-normal">Tracker</span>
          </span>
        </a>

        {/* ── Desktop nav ─────────────────────────────────────────────────── */}
        <NavigationMenu className="hidden md:flex">
          <NavigationMenuList>
            {NAV_LINKS.map(({ label, href }) => (
              <NavigationMenuItem key={label}>
                <NavigationMenuLink
                  href={href}
                  className={navigationMenuTriggerStyle()}
                >
                  {label}
                </NavigationMenuLink>
              </NavigationMenuItem>
            ))}
          </NavigationMenuList>
        </NavigationMenu>

        {/* ── Desktop auth ────────────────────────────────────────────────── */}
        <div className="hidden md:flex items-center">
          {user ? (
            <DropdownMenu>
              <DropdownMenuTrigger asChild>
                <button
                  className="flex items-center gap-2.5 rounded-full pl-1 pr-3 py-1 hover:bg-accent transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring"
                  aria-label="User menu"
                >
                  <Avatar className="h-8 w-8">
                    {user.avatarUrl && (
                      <AvatarImage src={user.avatarUrl} alt={user.name} />
                    )}
                    <AvatarFallback>{initials}</AvatarFallback>
                  </Avatar>
                  <span className="text-sm font-medium">{user.name}</span>
                </button>
              </DropdownMenuTrigger>
              <DropdownMenuContent align="end" className="w-48">
                <DropdownMenuLabel className="font-normal">
                  <p className="text-sm font-medium">{user.name}</p>
                </DropdownMenuLabel>
                <DropdownMenuSeparator />
                <DropdownMenuItem onClick={onProfile}>
                  <User />
                  Profile
                </DropdownMenuItem>
                <DropdownMenuItem onClick={onMyTeams}>
                  <Users />
                  My Teams
                </DropdownMenuItem>
                <DropdownMenuSeparator />
                <DropdownMenuItem
                  onClick={onSignOut}
                  className="text-destructive focus:text-destructive"
                >
                  <LogOut />
                  Sign out
                </DropdownMenuItem>
              </DropdownMenuContent>
            </DropdownMenu>
          ) : (
            <Button
              variant="outline"
              size="sm"
              onClick={onSignIn}
              className="gap-2"
            >
              <LogIn className="h-4 w-4" />
              Sign in
            </Button>
          )}
        </div>

        {/* ── Mobile hamburger ────────────────────────────────────────────── */}
        <Button
          variant="ghost"
          size="icon"
          className="md:hidden"
          aria-label={mobileOpen ? "Close menu" : "Open menu"}
          aria-expanded={mobileOpen}
          onClick={() => setMobileOpen((prev) => !prev)}
        >
          {mobileOpen ? (
            <X className="h-5 w-5" />
          ) : (
            <Menu className="h-5 w-5" />
          )}
        </Button>
      </div>

      {/* ── Mobile drawer ─────────────────────────────────────────────────── */}
      {mobileOpen && (
        <div className="md:hidden border-t bg-background animate-in slide-in-from-top-2 duration-200">
          <nav
            className="flex flex-col gap-1 px-4 py-3"
            aria-label="Mobile navigation"
          >
            {NAV_LINKS.map(({ label, href, icon: Icon }) => (
              <a
                key={label}
                href={href}
                onClick={() => setMobileOpen(false)}
                className={cn(
                  "flex items-center gap-3 rounded-md px-3 py-2.5 text-sm font-medium text-muted-foreground",
                  "hover:bg-accent hover:text-accent-foreground transition-colors",
                )}
              >
                <Icon className="h-4 w-4 shrink-0" />
                {label}
              </a>
            ))}
          </nav>

          {/* Mobile auth */}
          <div className="border-t px-4 py-3">
            {user ? (
              <div className="flex flex-col gap-1">
                <div className="flex items-center gap-3 px-3 py-2">
                  <Avatar className="h-8 w-8">
                    {user.avatarUrl && (
                      <AvatarImage src={user.avatarUrl} alt={user.name} />
                    )}
                    <AvatarFallback>{initials}</AvatarFallback>
                  </Avatar>
                  <span className="text-sm font-medium">{user.name}</span>
                </div>
                <button
                  onClick={() => {
                    onProfile?.();
                    setMobileOpen(false);
                  }}
                  className="flex items-center gap-3 rounded-md px-3 py-2.5 text-sm font-medium text-muted-foreground hover:bg-accent hover:text-accent-foreground transition-colors w-full text-left"
                >
                  <User className="h-4 w-4 shrink-0" />
                  Profile
                </button>
                <button
                  onClick={() => {
                    onMyTeams?.();
                    setMobileOpen(false);
                  }}
                  className="flex items-center gap-3 rounded-md px-3 py-2.5 text-sm font-medium text-muted-foreground hover:bg-accent hover:text-accent-foreground transition-colors w-full text-left"
                >
                  <Users className="h-4 w-4 shrink-0" />
                  My Teams
                </button>
                <button
                  onClick={() => {
                    onSignOut?.();
                    setMobileOpen(false);
                  }}
                  className="flex items-center gap-3 rounded-md px-3 py-2.5 text-sm font-medium text-destructive hover:bg-destructive/10 transition-colors w-full text-left"
                >
                  <LogOut className="h-4 w-4 shrink-0" />
                  Sign out
                </button>
              </div>
            ) : (
              <Button
                variant="outline"
                className="w-full gap-2"
                onClick={() => {
                  onSignIn?.();
                  setMobileOpen(false);
                }}
              >
                <LogIn className="h-4 w-4" />
                Sign in
              </Button>
            )}
          </div>
        </div>
      )}
    </header>
  );
}
