import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import logo from "@/assets/images/LogoPl2.webp";
import {
  NavigationMenu,
  NavigationMenuItem,
  NavigationMenuLink,
  NavigationMenuList,
  navigationMenuTriggerStyle,
} from "./ui/navigation-menu";
import { cn } from "@/lib/utils";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "./ui/dropdown-menu";
import { Avatar, AvatarFallback, AvatarImage } from "./ui/avatar";
import { Button } from "./ui/button";
import {
  Sheet,
  SheetContent,
  SheetHeader,
  SheetTitle,
  SheetTrigger,
} from "./ui/sheet";
import { Separator } from "./ui/separator";
import {
  LogIn,
  Menu,
  User,
  Users,
  LogOut,
  Home,
  Shield,
  CalendarDays,
  Star,
} from "lucide-react";

// Nav Links
const NAV_LINKS = [
  { label: "Home", href: "/", icon: Home },
  { label: "Teams", href: "/teams", icon: Shield },
  { label: "Players", href: "/players", icon: User },
  { label: "Matches", href: "/matches", icon: CalendarDays },
  { label: "Top scorers", href: "/top-scorers", icon: Star },
] as const;

// Mock user state — replace with actual auth logic
const MOCK_USER = {
  name: "John Doe",
  avatarUrl: undefined,
};

export default function Navbar() {
  const [sheetOpen, setSheetOpen] = useState(false);
  const navigate = useNavigate();

  const closeSheet = () => setSheetOpen(false);

  /** Initials fallback for avatar */
  const initials = MOCK_USER?.name
    .split(" ")
    .map((n) => n[0])
    .join("")
    .slice(0, 2)
    .toUpperCase();

  return (
    <header className="sticky top-0 z-50 w-full border-b bg-purple-800  shadow-lg text-white">
      {/* ── Logo ───────────────────────────────────────────────────────── */}
      <div className="flex h-16  justify-between items-center mx-4 p-4">
        <Link to="/" className="flex items-center gap-2 px-2  ">
          <img src={logo} alt="Premier League Tracker" className="w-10" />
          {/* desktop*/}
          <span className="hidden md:block font-bold ">
            Premier League Tracker
          </span>
          {/* mobile */}
          <span className="md:hidden font-semibold ">PL Tracker</span>
        </Link>

        {/* ── Desktop nav ─────────────────────────────────────────────────── */}
        <NavigationMenu className="hidden md:flex">
          <NavigationMenuList>
            {NAV_LINKS.map(({ label, href }) => (
              <NavigationMenuItem key={label}>
                <NavigationMenuLink asChild>
                  <Link
                    to={href}
                    className={cn(
                      navigationMenuTriggerStyle(),
                      "bg-transparent hover:bg-white/10 hover:text-white focus:bg-white/10 focus:text-white transition-colors duration-300",
                    )}
                  >
                    {label}
                  </Link>
                </NavigationMenuLink>
              </NavigationMenuItem>
            ))}
          </NavigationMenuList>
        </NavigationMenu>

        {/* ── Desktop auth ─────────────────────────────────────────────────── */}
        <div className="hidden md:flex">
          {MOCK_USER ? (
            <DropdownMenu>
              <DropdownMenuTrigger asChild>
                <button
                  className="flex items-center gap-2 rounded-full hover:bg-purple-700 transition-colors pl-1 pr-3 py-1 "
                  aria-label="User menu"
                >
                  <Avatar className="h-8 w-8">
                    {MOCK_USER.avatarUrl ? (
                      <AvatarImage
                        src={MOCK_USER.avatarUrl}
                        alt={MOCK_USER.name}
                      />
                    ) : (
                      <AvatarFallback className="text-purple-800">
                        {initials}
                      </AvatarFallback>
                    )}
                  </Avatar>
                  <span className="text-sm font-medium">{MOCK_USER.name}</span>
                </button>
              </DropdownMenuTrigger>
              <DropdownMenuContent align="end" className="w-48">
                <DropdownMenuLabel>{MOCK_USER.name}</DropdownMenuLabel>
                <DropdownMenuSeparator />
                <DropdownMenuItem onClick={() => navigate("/profile")}>
                  <User />
                  Profile
                </DropdownMenuItem>
                <DropdownMenuItem onClick={() => navigate("/my-teams")}>
                  <Users />
                  My Teams
                </DropdownMenuItem>
                <DropdownMenuSeparator />
                <DropdownMenuItem
                  onClick={() => navigate("/sign-out")}
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
              onClick={() => navigate("/sign-in")}
              className="gap-2"
            >
              <LogIn className="h-4 w-4" />
              Sign in
            </Button>
          )}
        </div>

        {/* ── Mobile hamburger ─────────────────────────────────────────────── */}
        <Sheet open={sheetOpen} onOpenChange={setSheetOpen}>
          <SheetTrigger asChild>
            <Button
              variant="ghost"
              size="icon"
              className="md:hidden hover:bg-white/10 text-white"
              aria-label="Open menu"
            >
              <Menu className="h-5 w-5" />
            </Button>
          </SheetTrigger>

          {/* ── Mobile drawer ──────────────────────────────────────────────── */}
          <SheetContent side="left" className="w-3/4 flex flex-col ">
            <SheetHeader className="px-4 py-4 border-b">
              <SheetTitle asChild>
                <Link
                  to="/"
                  onClick={closeSheet}
                  className="flex items-center gap-2"
                >
                  <img
                    src={logo}
                    alt="Premier League Tracker"
                    className="w-8"
                  />
                  <span className="font-bold text-sm leading-tight">
                    Premier League
                    <br />
                    <span className="font-normal text-muted-foreground">
                      Tracker
                    </span>
                  </span>
                </Link>
              </SheetTitle>
            </SheetHeader>

            {/* Nav links */}
            <nav className="flex flex-col gap-1 px-3 py-3">
              {NAV_LINKS.map(({ label, href, icon: Icon }) => (
                <Link
                  key={label}
                  to={href}
                  onClick={closeSheet}
                  className="flex items-center gap-3 rounded-md px-3 py-2.5 text-sm font-medium text-muted-foreground hover:bg-accent hover:text-accent-foreground transition-colors"
                >
                  <Icon className="h-4 w-4 shrink-0" />
                  {label}
                </Link>
              ))}
            </nav>

            {/* ── Mobile auth ─────────────────────────────────────────────── */}
            <div>
              <Separator />
              {MOCK_USER ? (
                <div className="flex flex-col gap-1 px-3 py-3">
                  {/* User info */}
                  <div className="flex items-center gap-3 px-3 py-2 mb-1">
                    <Avatar className="h-9 w-9">
                      {MOCK_USER.avatarUrl ? (
                        <AvatarImage
                          src={MOCK_USER.avatarUrl}
                          alt={MOCK_USER.name}
                        />
                      ) : (
                        <AvatarFallback>{initials}</AvatarFallback>
                      )}
                    </Avatar>
                    <div className="flex flex-col">
                      <span className="text-sm font-medium leading-none">
                        {MOCK_USER.name}
                      </span>
                    </div>
                  </div>
                  <Separator className="mb-1" />
                  <Button
                    variant="ghost"
                    className="justify-start gap-3 px-3 h-10 font-normal text-muted-foreground"
                    onClick={() => {
                      navigate("/profile");
                      closeSheet();
                    }}
                  >
                    <User className="h-4 w-4 shrink-0" />
                    Profile
                  </Button>
                  <Button
                    variant="ghost"
                    className="justify-start gap-3 px-3 h-10 font-normal text-muted-foreground"
                    onClick={() => {
                      navigate("/my-teams");
                      closeSheet();
                    }}
                  >
                    <Users className="h-4 w-4 shrink-0" />
                    My Teams
                  </Button>
                  <Button
                    variant="ghost"
                    className="justify-start gap-3 px-3 h-10 font-normal text-destructive hover:text-destructive hover:bg-destructive/10"
                    onClick={() => {
                      navigate("/sign-out");
                      closeSheet();
                    }}
                  >
                    <LogOut className="h-4 w-4 shrink-0" />
                    Sign out
                  </Button>
                </div>
              ) : (
                <div className="px-4 py-4">
                  <Button
                    className="w-full gap-2"
                    onClick={() => {
                      navigate("/sign-in");
                      closeSheet();
                    }}
                  >
                    <LogIn className="h-4 w-4" />
                    Sign in
                  </Button>
                </div>
              )}
            </div>
          </SheetContent>
        </Sheet>
      </div>
    </header>
  );
}
