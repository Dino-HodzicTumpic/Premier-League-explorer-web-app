# 🏆 Premier League Tracker

Premier League Explorer is a full-stack web application that provides Premier League standings, fixtures, live match tracking, detailed statistics, lineups, and AI-generated football news in a modern and responsive interface.

Built with a Java Spring Boot backend and a React frontend, the platform architecture incorporates memory caching, scheduled background jobs, and multi-source API aggregation. Latest football headlines are enriched into full articles using the Gemini API.

## 🚀 System Architecture Overview

The backend is built using an **N-Tier Layered Architecture** (Controller-Service-Repository) designed around stateless API principles. DTOs are used to separate API contracts from database entities, while ModelMapper handles conversion between layers.

## 🛠 Tech Stack

* **Frontend:** React, TypeScript, TanStack Query (React Query), Tailwind CSS, Shadcn/ui.
* **Backend:** Java Spring Boot (Web, Data JPA), Spring Scheduling, Caffeine Cache, Lombok, ModelMapper.
* **Database & Migrations:** PostgreSQL, Liquibase (Version-controlled schema updates).
* **AI Integration:** Google Gemini API (Generative AI).
* **Data Sources:** ESPN API (Unofficial endpoints for live feeds), Football-Data.org (standings, top-scorers).

---

## ✨ Key Features

* **📅 Match Center (`/matches`):**
  Interactive gameweek-based system displaying all Premier League fixtures and results.
  Includes detailed match view with:
  - Event timeline (goals, assists, cards, substitutions)
  - Match statistics (possession, shots, passes, fouls, etc.)
  - Tactical lineups and formations

* **🏆 Standings Dashboard (`/standings`):**
  Displays the current Premier League table including points, goal difference, wins, draws, losses, and goals scored/conceded.

* **🎯 Top Scorers (`/top-scorers`):**
  Shows the top 20 goal scorers with full player and club context.

* **🤖 News (`/news`):**
  Fetches latest football headlines from ESPN API and uses Google Gemini to generate full-length articles.
  Articles are stored in the database and displayed as the latest 5 news entries.

---

## 📸 Screenshots

| Standings Dashboard | Top Scorers | AI News Feed |
| :--- | :--- | :--- |
| ![Standings](screenshots/Standings.png) | ![Top Scorers](screenshots/TopScorers.png) | ![News Feed](screenshots/News.png) |

<details>
  <summary>📸 Click to view detailed Match Center layouts (Lineups, Stats, Timelines)</summary>
  
  #### Match list and GameWeek selector
  ![Match list](screenshots/MatchList.png)
  
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
Navigate to `backend/src/main/resources/application.properties` and configure your environment variables:
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
