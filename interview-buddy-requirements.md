# InterviewBuddy — Requirements Document

> A personal interview preparation companion for developers.
> Curated questions, mock sessions, and progress tracking — all in one place.

---

## 1. Project Overview

**App Name:** InterviewBuddy  
**Purpose:** Help developers prepare for technical interviews through a curated question bank, mock session simulation, and personal progress tracking.  
**Target Users:** Single user (developer / interviewer) with potential for colleague sharing in future phases.  
**Tone:** Positive, empowering — this is a prep tool, not an evaluation tool.

---

## 2. Tech Stack

### Backend
- Java 17
- Spring Boot 3.x
- Spring Data JPA (Hibernate)
- Spring Security (basic auth — single user)
- PostgreSQL (via Docker)
- Flyway (DB migrations)
- Maven

### Frontend
- Angular 20
- Angular Material (UI components)
- Chart.js / ng2-charts (progress dashboard)
- Angular Standalone Components

### Infrastructure
- Docker Compose (PostgreSQL + pgAdmin)
- H2 (for local unit tests only)

---

## 3. Repository Structure

```
interview-buddy/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/interviewbuddy/
│   │   │   │   ├── config/
│   │   │   │   ├── controller/
│   │   │   │   ├── domain/
│   │   │   │   ├── dto/
│   │   │   │   ├── repository/
│   │   │   │   └── service/
│   │   │   └── resources/
│   │   │       ├── application.yml
│   │   │       └── db/migration/
│   │   └── test/
│   └── pom.xml
├── frontend/
│   ├── src/
│   │   ├── app/
│   │   │   ├── core/
│   │   │   ├── shared/
│   │   │   └── features/
│   │   │       ├── admin/
│   │   │       ├── browse/
│   │   │       ├── session/
│   │   │       └── dashboard/
│   │   └── environments/
│   └── angular.json
├── docker-compose.yml
├── agents.md
├── CLAUDE.md
└── README.md
```

---

## 4. Domain Model

### 4.1 Entities

#### `Question`
| Field | Type | Notes |
|---|---|---|
| id | UUID | PK |
| title | String | Short question title |
| body | String (Text) | Full question text |
| answer | String (Text) | Model answer |
| followUpProbes | String (Text) | Optional probing follow-ups |
| topic | Enum | See Topic list below |
| techStack | Enum | See TechStack list below |
| difficultyLevel | Enum | EASY / MEDIUM / HARD |
| experienceRangeMin | Integer | Years (e.g. 2) |
| experienceRangeMax | Integer | Years (e.g. 5) |
| tags | List<String> | Free-form tags |
| isActive | Boolean | Soft delete / hide |
| createdAt | Timestamp | Auto |
| updatedAt | Timestamp | Auto |

#### `Topic` (Enum)
`JAVA_CORE`, `COLLECTIONS`, `CONCURRENCY`, `JVM_INTERNALS`, `SPRING_BOOT`, `SPRING_SECURITY`, `HIBERNATE_JPA`, `MICROSERVICES`, `DESIGN_PATTERNS`, `SOLID`, `TDD`, `SYSTEM_DESIGN`, `BEHAVIOURAL`

#### `TechStack` (Enum)
`JAVA`, `SPRING`, `ANGULAR`, `REACT`, `POSTGRES`, `KAFKA`, `DOCKER`, `KUBERNETES`, `AWS`, `GENERAL`

#### `DifficultyLevel` (Enum)
`EASY`, `MEDIUM`, `HARD`

---

#### `MockSession`
| Field | Type | Notes |
|---|---|---|
| id | UUID | PK |
| name | String | e.g. "Java Senior Round 1" |
| topic | Enum | Filter used |
| techStack | Enum | Filter used |
| difficultyLevel | Enum | Filter used |
| experienceRange | Integer | Years filter used |
| questionCount | Integer | How many questions in session |
| status | Enum | IN_PROGRESS / COMPLETED |
| startedAt | Timestamp | |
| completedAt | Timestamp | Nullable |

#### `SessionAttempt`
| Field | Type | Notes |
|---|---|---|
| id | UUID | PK |
| session | MockSession | FK |
| question | Question | FK |
| confidence | Enum | CONFIDENT / NEEDS_WORK / SKIPPED |
| personalNote | String (Text) | User's own notes |
| attemptedAt | Timestamp | |

#### `Bookmark`
| Field | Type | Notes |
|---|---|---|
| id | UUID | PK |
| question | Question | FK |
| note | String | Optional note |
| createdAt | Timestamp | |

---

## 5. Features

### 5.1 Pre-Built (implemented before the demo starts)

Everything below is fully built and working before the session begins.
These are not demoed as agent output — they are the existing app the agent will extend.

#### F1: Question Bank (Admin)
- Add / edit / deactivate questions with all fields
- List view with filters (topic, stack, difficulty, experience range)
- Question detail view with answer and follow-up probes

#### F2: Browse & Filter (Main UI)
- Filter questions by topic, tech stack, difficulty, experience range
- Question cards with title, tags, difficulty badge
- Expand card to see full answer and probes

#### F3: Mock Session
- Configure a session (topic, stack, difficulty, experience, question count)
- Random question selection matching config
- One question at a time — mark Confident / Needs Work / Skipped
- Add personal notes per question
- Session summary with confidence breakdown

#### F4: Session History & Review
- List of all past sessions
- Click to review questions + confidence + notes from any past session
- Re-attempt a session (creates a new session with same config)

#### F5: Progress Dashboard
- Topic-wise confidence breakdown chart
- Weak areas highlight (topics with most "Needs Work")
- Sessions completed per week (streak view)

---

### 5.2 ⭐ SDD Demo Feature — Add Note to Bookmark

**Scope:** Deliberately small. Single endpoint, single UI interaction. Runs in under 2 minutes.

**What exists before demo:** Bookmarks work — user can add/remove bookmarks on any question. But there is no way to attach a personal note to a bookmark yet.

**What the agent builds during demo:**
- Backend: `PUT /api/bookmarks/{id}/note` — accepts `{ "note": "string" }`, updates the bookmark note
- Frontend: Inline edit field on the Bookmark list page — pencil icon opens a text input, save button calls the API

**SDD spec handed to agent:**
```
Feature: Add note to bookmark
As a user, I want to add a personal note to any bookmarked question
so I can remind myself why I saved it or what I need to revisit.

Acceptance Criteria:
- User can click an edit icon next to any bookmark
- An inline text input appears pre-filled with existing note (if any)
- User types a note and clicks Save
- Note persists via PUT /api/bookmarks/{id}/note
- Note is visible on the bookmark card after saving
```

**Why this works for demo:** One entity already exists, one new endpoint, one UI change. Agent has enough context to complete it without going off-track. Audience sees the full backend → frontend cycle cleanly.

---

### 5.3 ⭐ OpenSpec Demo Feature — Difficulty Chip Filter on Browse Page

**Scope:** UI-only enhancement. No new entity, no new API. Runs in under 2 minutes.

**What exists before demo:** Browse page has a filter sidebar with dropdowns for topic, stack, experience range. Difficulty filter exists as a dropdown too.

**What the agent builds during demo:**
- Replace the difficulty dropdown with a visual chip/toggle bar: `[ ALL ] [ EASY ] [ MEDIUM ] [ HARD ]`
- Chips are colour-coded: green / amber / red
- Selecting a chip filters the question list instantly (client-side, no new API call needed)
- Selected chip is highlighted

**OpenSpec phase gates:**

*Explore:*
- Agent reads `browse.component.ts`, `question-filter.model.ts`, existing filter sidebar
- Maps current difficulty dropdown binding and filter method
- Reports: "Found difficultyLevel filter bound to dropdown in filter sidebar. No API change needed."

*Propose:*
- Agent proposes: replace mat-select with mat-chip-listbox, add colour classes per difficulty
- Lists files to change: `browse.component.ts`, `browse.component.html`, `browse.component.scss`
- Awaits approval before writing any code

*Implement:*
- Agent makes only the changes listed in the Propose output
- No scope creep, no touching other filters

**Why this works for OpenSpec demo:** Explore phase has something real to read. Propose phase shows meaningful planning. Phase gate is visible — agent cannot skip to Implement. Change is visual and instant for the audience to see.

---

### 5.4 Phase 3 — Future / AI Features (out of scope for demo)

- AI answer evaluator — paste your answer, get feedback
- Question generator from JD or topic
- Multi-user support
- Spaced repetition for weak areas
- Import / export question bank (JSON)

---

## 6. API Design

### Questions
```
GET     /api/questions              # List with filters
GET     /api/questions/{id}         # Single question
POST    /api/questions              # Create
PUT     /api/questions/{id}         # Update
DELETE  /api/questions/{id}         # Soft delete
GET     /api/questions/topics       # Enum list
GET     /api/questions/stacks       # Enum list
```

### Sessions
```
GET     /api/sessions               # List all sessions
POST    /api/sessions               # Create + configure
GET     /api/sessions/{id}          # Session with questions
PUT     /api/sessions/{id}/complete # Mark complete
GET     /api/sessions/history       # Past sessions list
```

### Attempts
```
POST    /api/sessions/{id}/attempts         # Submit attempt
PUT     /api/sessions/{id}/attempts/{aid}   # Update attempt
GET     /api/sessions/{id}/attempts         # All attempts in session
```

### Bookmarks
```
GET     /api/bookmarks              # List bookmarks
POST    /api/bookmarks              # Add bookmark
DELETE  /api/bookmarks/{id}         # Remove bookmark
PUT     /api/bookmarks/{id}/note    # ⭐ SDD DEMO — Update note on bookmark
```

### Dashboard
```
GET     /api/dashboard/summary      # Topic confidence breakdown
GET     /api/dashboard/streak       # Weekly streak
GET     /api/dashboard/weak-areas   # Most "needs work" topics
```

---

## 7. Frontend Pages

| Route | Page | Status |
|---|---|---|
| `/admin/questions` | Question bank list | Pre-built |
| `/admin/questions/new` | Add question form | Pre-built |
| `/admin/questions/:id/edit` | Edit question | Pre-built |
| `/browse` | Browse + filter (⭐ OpenSpec chip filter here) | Pre-built + demo |
| `/browse/:id` | Question detail | Pre-built |
| `/sessions/new` | Configure session | Pre-built |
| `/sessions/:id` | Active session | Pre-built |
| `/sessions/:id/summary` | Session summary | Pre-built |
| `/sessions/history` | Past sessions | Pre-built |
| `/sessions/:id/review` | Review past session | Pre-built |
| `/bookmarks` | Bookmarks (⭐ SDD note edit here) | Pre-built + demo |
| `/dashboard` | Progress charts | Pre-built |

---

## 8. Docker Compose

```yaml
version: '3.8'

services:
  postgres:
    image: postgres:16
    container_name: interview-buddy-db
    environment:
      POSTGRES_DB: interviewbuddy
      POSTGRES_USER: ibuser
      POSTGRES_PASSWORD: ibpass
    ports:
      - "5432:5432"
    volumes:
      - pgdata:/var/lib/postgresql/data

  pgadmin:
    image: dpage/pgadmin4
    container_name: interview-buddy-pgadmin
    environment:
      PGADMIN_DEFAULT_EMAIL: admin@interviewbuddy.dev
      PGADMIN_DEFAULT_PASSWORD: admin
    ports:
      - "5050:80"
    depends_on:
      - postgres

volumes:
  pgdata:
```

---

## 9. Application Config (`application.yml`)

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/interviewbuddy
    username: ibuser
    password: ibpass
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true
  flyway:
    enabled: true
    locations: classpath:db/migration

server:
  port: 8080

interview-buddy:
  cors:
    allowed-origins: http://localhost:4200
```

---

## 10. Demo Script

### Act 3 — Plain SDD Demo (2 min)
```
1. Show the SDD spec for "Add note to bookmark" (30 sec)
2. Hand spec to Copilot agent — agent generates PUT endpoint + inline edit UI (60 sec)
3. Run app — click pencil on a bookmark, type note, save, note appears (30 sec)
Done.
```

### Act 5 — OpenSpec Demo (2 min)
```
1. Open opsx with the Difficulty Chip Filter spec (20 sec)
2. Explore phase — agent reads browse component, reports what it found (30 sec)
3. Propose phase — agent lists exactly 3 files it will change, awaits approval (20 sec)
4. Approve — Implement phase runs (30 sec)
5. Show browse page — chips replace dropdown, click HARD, list filters (20 sec)
Done.
```

---

## 11. agents.md (Copilot Context File)

```markdown
# InterviewBuddy — Agent Context

## What this app is
A personal interview preparation companion. Helps developers practice with
curated questions, run mock sessions, and track progress over time.
This is a prep and learning tool — NOT an interviewer evaluation tool.

## Tech stack
- Backend: Java 17, Spring Boot 3.x, PostgreSQL, Flyway, Maven
- Frontend: Angular 20, Angular Material, Chart.js
- DB: PostgreSQL via Docker Compose

## Conventions
- All entities use UUID primary keys
- DTOs are separate from domain entities — never expose entities directly
- Services handle all business logic — controllers are thin
- Flyway manages all schema changes — never use ddl-auto: create
- Angular components are standalone
- API base path: /api

## What NOT to do
- Do not add Spring Security complexity beyond basic config for now
- Do not use Lombok — explicit getters/setters only
- Do not modify existing Flyway migration files — create a new one
- Do not put business logic in controllers or repositories
- Do not change working features when adding new ones
```

---

## 12. CLAUDE.md (Claude Agent Context File)

```markdown
# InterviewBuddy — Claude Context

## App purpose
Interview preparation companion for developers. Question bank + mock sessions +
progress tracking. Positive framing — prep tool, not evaluation tool.

## Stack
Java 17 / Spring Boot 3 / PostgreSQL / Flyway / Angular 20 / Angular Material

## Key conventions
- UUID PKs on all entities
- DTO layer mandatory — no entity exposure via API
- Thin controllers, logic in services
- Flyway for all migrations, validate mode in JPA
- Angular standalone components
- Single user — no multi-tenancy needed

## Boundaries
- No Lombok
- No ddl-auto create/update — Flyway only
- No business logic in controllers
- New migration file for every schema change
- Do not touch working features when extending
```

---

*Document version: 2.0 | Demo features scoped to 2-min runs | Seed data: see V2__seed_questions.sql*
