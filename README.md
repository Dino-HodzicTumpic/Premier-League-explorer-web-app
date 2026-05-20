# 🏆 Premier League Tracker

A comprehensive, production-grade full-stack web application designed for football enthusiasts to track English Premier League standings, match fixtures, comprehensive statistical centers, and AI-generated editorial content. 

Built with a **Java Spring Boot** backend and a **React** frontend, the platform architecture incorporates memory caching, background polling schedulers, multi-source external API aggregation, and automated generative AI workflows to dynamically synthesize complete news articles from raw headlines.

## 🚀 System Architecture Overview

The backend is built using an **N-Tier Layered Architecture** (Controller-Service-Repository) designed around stateless API principles. It decouples core business logic from database entities by utilizing specialized Data Transfer Objects (DTOs) and `ModelMapper`.

## 🛠 Tech Stack

* **Frontend:** React, TypeScript, TanStack Query (React Query), Tailwind CSS, Shadcn UI.
* **Backend:** Java 17, Spring Boot (Web, Data JPA), Spring Scheduling, Caffeine Cache, Lombok, ModelMapper.
* **Database & Migrations:** PostgreSQL, Liquibase (Version-controlled schema updates).
* **AI Integration:** Google Gemini Pro API (Generative AI).
* **Data Sources:** ESPN API (Unofficial endpoints for live feeds), Football-Data.org (Core structural metrics).

---

## ✨ Key Features

* **🏆 Live Standings Dashboard (`/standings`):** Displays real-time updated league tables tracking standard competition parameters (Points, Goal Difference, Wins, Draws, Losses, Goals For/Against).
* **🎯 Top Scorers Hub (`/top-scorers`):** Monitors the top 20 goalscorers in the league, enriched with club crests, player assets, and official scoring statistics.
* **📅 Dynamic Match Center (`/matches`):**
    * **Interactive GameWeek Selector:** Allows quick historical navigation across all 38 gameweeks via an overlay dropdown interface.
    * **Triple-Tab Match Analytics:**
        * *Details:* Chronological game event timeline mapping goals, assists, player substitutions, and bookings with minute-accurate timestamps.
        * *Stats:* Graphical comparative telemetry tracking parameters like possession %, corner kicks, passing accuracy, fouls, saves, and total shots.
        * *Lineups:* A visually modeled tactical pitch grid rendering starting eleven formations and exact in-game positioning.
* **🤖 AI-Driven News Engine (`/news`):**
    * Automatically fetches raw headline data from foreign sport endpoints (ESPN API).
    * Uses a custom background service pipeline to pipe headline arrays into **Google Gemini Pro AI**, prompting it to research, expand, and synthesize full-length structured analytical reports.
    * Persists generated content to the database, serving a responsive editorial board of the 5 freshest entries.

---

## 📸 Screenshots

| Standings Dashboard | Top Scorers | AI News Feed |
| :--- | :--- | :--- |
| ![Standings](screenshots/Standings.png) | ![Top Scorers](screenshots/TopScorers.png) | ![News Feed](screenshots/NewsFeed.png) |

<details>
  <summary>📸 Click to view detailed Match Center layouts (Lineups, Stats, Timelines)</summary>
  
  #### GameWeek Selector
  ![GameWeek Selector](screenshots/GameWeekSelector.png)
  
  #### Match Details & Timeline
  ![Match Details](screenshots/MatchDetails.png)
  
  #### Team Statistics Comparison
  ![Match Stats](screenshots/MatchStats.png)
  
  #### Tactical Lineup Visualization
  ![Match Lineups](screenshots/MatchLineups.png)
</details>

---

## ⚙️ Core Technical Highlights (Backend Implementation)

### ⚡ Dual-Stage Cache Polling & Schedulers
To bypass strict free-tier consumption limits and preserve low UI response times, the backend decouples live matching updates into a dual-stage scheduler model managed through **Caffeine Cache**:
* **`LiveMatchListScheduler` (Every 20 seconds):** Polls the primary daily scoreboards. It actively partitions active games into a high-frequency cache layer (`liveMatchListCache`) streaming localized telemetry (minutes, injury time, real-time scores) as lightweight `LiveMatchSnapshotDto` entities.
* **`LiveMatchDetailsScheduler` (Every 60 seconds):** Dynamically looks up keys within the live cache, querying deep match summary details (events, formations, stats) only for ongoing fixtures and writing them into a dedicated details cache.

### 🔄 Asynchronous Game Finalization & Idempotent Upserts
* When the live tracker scheduler captures an incoming state transition change from *LIVE* to *ENDED*, it flags the record as `ENDED_PENDING_PERSIST` and routes it to `MatchFinalizationService`.
* To handle unreliable external identifiers, the system implements an **Idempotent Upsert Strategy** inside `EspnEventUpsertService`. Before parsing incoming statistics, it drops transactional event tables associated with the targeted Match ID (`goalRepository.deleteByMatch(managedMatch)`, etc.) and re-hydrates the schema completely fresh, eliminating any probability of duplicate data.

### 🛡️ Defensive Player Creation Pipeline
* Real-time feeds occasionally submit un-indexed players during match events (e.g., academy players making debuts). The ingestion logic uses an entity warm-up algorithm that dynamically checks local records via missing ID sets and builds newly validated `Player` profiles on the fly without breaking the active transactional process.

### 📦 Database Migration Versioning
* Relies on **Liquibase** instead of fragile native Hibernate auto-generation options (`ddl-auto: update`). This enforces strict, source-controlled database changes, ensuring clear incremental safety constraints when publishing schema updates across isolated team dependencies.

---

## 🛣️ Project Roadmap

* [ ] **Stateless Authentication:** Integrate Spring Security coupled with JSON Web Token (JWT) tracking and Google OAuth2 providers.
* [ ] **Deployment Framework:** Standardize localized infrastructure into containerized configurations utilizing multi-stage Docker builds.
* [ ] **Unified Search Mechanism:** Introduce optimized composite database query layers to seamlessly discover players, managers, and clubs from a centralized toolbar.

---

## ⚙️ Local Installation & Setup

### Prerequisites
* Java 17 SDK (or newer)
* Node.js v18+
* PostgreSQL Instance

### 1. Database Setup
Create a local PostgreSQL database named `pl_explorer`.

### 2. Backend Configuration
Navigate to `backend/src/main/resources/application.properties` (or `application.yml`) and configure your environment variables:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/pl_explorer
spring.datasource.username=your_postgres_user
spring.datasource.password=your_postgres_password

# Gemini AI Configuration
gemini.api.key=your_gemini_api_key_here

cd backend
./mvnw spring-boot:run

cd frontend
npm install
npm run dev
