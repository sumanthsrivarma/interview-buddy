# InterviewBuddy — Agent Instructions

> Primary context for GitHub Copilot agent mode. Read in full before generating any code.

---

## 1. What This App Is

InterviewBuddy is a single-user technical interview preparation tool: question bank, mock session simulation with confidence tracking, bookmarks with personal notes, and a progress dashboard. There is no multi-tenancy, no AI evaluation, no job board, and no public registration. AI feedback, spaced repetition, and import/export are explicitly out of scope.

---

## 2. Tech Stack

| Layer | Technology | Version |
|---|---|---|
| Backend language | Java | 17 |
| Backend framework | Spring Boot | 3.x |
| Persistence | Spring Data JPA (Hibernate) | via Spring Boot 3.x |
| Database | PostgreSQL | 16 |
| DB migrations | Flyway | via Spring Boot 3.x |
| Build tool | Maven | 3.x |
| Security | Spring Security | Basic auth, single-user |
| Frontend framework | Angular | 20 |
| UI components | Angular Material | aligned with Angular 20 |
| Charts | Chart.js via ng2-charts | progress dashboard only |
| Frontend component model | Angular Standalone Components | no NgModules |
| Test database | H2 | unit tests only — never in production config |
| Infrastructure | Docker Compose | PostgreSQL 16 + pgAdmin |

**Do not upgrade, swap, or add runtime dependencies without explicit instruction.**

---

## 3. Repository Structure

```
interview-buddy/
├── backend/                        # Spring Boot application root
│   ├── pom.xml                     # Maven build descriptor
│   └── src/
│       ├── main/
│       │   ├── java/com/interviewbuddy/
│       │   │   ├── config/         # Spring Security and CORS configuration
│       │   │   ├── controller/     # REST controllers (@RestController)
│       │   │   ├── domain/         # JPA entities and enums
│       │   │   ├── dto/            # Request and response DTOs (no domain objects on the wire)
│       │   │   ├── repository/     # Spring Data JPA repositories
│       │   │   └── service/        # Business logic layer
│       │   └── resources/
│       │       ├── application.yml # All application config
│       │       └── db/migration/   # Flyway versioned SQL scripts (V{n}__description.sql)
│       └── test/                   # Unit and integration tests (H2 in-memory)
├── frontend/                       # Angular 20 application root
│   ├── angular.json
│   ├── package.json
│   ├── proxy.conf.json             # Dev proxy: /api → http://localhost:8080
│   └── src/
│       ├── app/
│       │   ├── core/
│       │   │   ├── models/         # TypeScript interfaces mirroring backend DTOs
│       │   │   └── services/       # Injectable services that call the backend API
│       │   ├── shared/             # Reusable standalone components and pipes
│       │   └── features/           # Feature modules, each in its own subfolder
│       │       ├── admin/          # Question bank CRUD (admin UI)
│       │       ├── browse/         # Question browsing, filtering, and detail view
│       │       ├── session/        # Mock session config, active session, history, review, summary
│       │       └── dashboard/      # Progress charts and weak-area highlights
│       └── environments/           # environment.ts (dev) and environment.prod.ts
├── docker-compose.yml              # Postgres 16 + pgAdmin services
├── AGENTS.md                       # This file — primary agent context
└── interview-buddy-requirements.md # Full requirements reference
```

---

## 4. Domain Vocabulary

Use these exact names — in code, in comments, and in generated SQL. Do not invent synonyms.

### Entities

| Entity | Java class | Table |
|---|---|---|
| Question | `Question` | `questions` |
| Mock session | `MockSession` | `mock_sessions` |
| Session attempt | `SessionAttempt` | `session_attempts` |
| Bookmark | `Bookmark` | `bookmarks` |

### Enums

**`Topic`**  
`JAVA_CORE`, `COLLECTIONS`, `CONCURRENCY`, `JVM_INTERNALS`, `SPRING_BOOT`, `SPRING_SECURITY`, `HIBERNATE_JPA`, `MICROSERVICES`, `DESIGN_PATTERNS`, `SOLID`, `TDD`, `SYSTEM_DESIGN`, `BEHAVIOURAL`

**`TechStack`**  
`JAVA`, `SPRING`, `ANGULAR`, `REACT`, `POSTGRES`, `KAFKA`, `DOCKER`, `KUBERNETES`, `AWS`, `GENERAL`

**`DifficultyLevel`**  
`EASY`, `MEDIUM`, `HARD`

**`Confidence`** (per `SessionAttempt`)  
`CONFIDENT`, `NEEDS_WORK`, `SKIPPED`

**`SessionStatus`** (on `MockSession`)  
`IN_PROGRESS`, `COMPLETED`

### Key Terms

- **Question bank** — the full set of questions managed via the admin UI.
- **Mock session** — a configured practice run selecting N questions by filter criteria.
- **Session attempt** — a single answer attempt within a mock session; records confidence and personal notes.
- **Bookmark** — a saved question reference; may carry an optional text note.
- **Soft delete** — questions are deactivated via `isActive = false`, never hard-deleted.
- **Experience range** — stored as `experienceRangeMin` and `experienceRangeMax` (integer years) on `Question`.
- **Personal note** — free-text field on `SessionAttempt` and on `Bookmark`; these are different fields on different entities.
- **Confidence breakdown** — aggregation of `Confidence` values across attempts, used on the dashboard.
- **Weak areas** — topics with the highest proportion of `NEEDS_WORK` attempts.
- **Weekly streak** — count of sessions completed per calendar week.