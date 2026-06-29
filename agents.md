# InterviewBuddy — Agent Instructions

> This file is the primary context source for GitHub Copilot agent mode.  
> Read this file in full before generating any code for this project.  

---

## 1. What This App Is

InterviewBuddy is a single-user technical interview preparation tool. It provides a curated question bank, mock session simulation with per-question confidence tracking, bookmarks with personal notes, and a progress dashboard with topic-wise breakdowns. The app is owned and used by one developer preparing for interviews — there is no multi-tenancy, no role hierarchy, and no public registration. This is **not** an AI answer evaluator, not a job board, not a recruiter-facing tool, and not a multi-user platform. Features such as AI feedback, question generation from a job description, spaced repetition, and import/export are explicitly out of scope and must not be introduced.

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

---

## 5. Architectural Constraints

These rules apply to every feature, in every layer, without exception.

### Backend

1. **No domain objects on the wire.** Controllers accept DTOs and return DTOs. Never expose a JPA entity directly from a controller method.
2. **Service layer owns all business logic.** Controllers delegate immediately to a `@Service` class. Controllers must not contain query logic, conditional branching on business rules, or direct repository calls.
3. **Repository layer is Spring Data JPA only.** Write derived query methods or `@Query` JPQL annotations. Do not write native SQL queries unless there is no JPQL equivalent and the reason is documented in a comment.
4. **All schema changes go through Flyway.** Never set `ddl-auto` to `create`, `create-drop`, or `update` in any environment other than H2 tests. Schema changes require a new `V{n}__description.sql` file.
5. **UUIDs as primary keys.** All entity IDs are `UUID`, generated by `@GeneratedValue(strategy = GenerationType.UUID)` or equivalent. Never use auto-increment integers as PKs on domain entities.
6. **Soft delete for questions.** Set `isActive = false`. Never issue a `DELETE` SQL statement for a `Question` row.
7. **Single-user security.** Spring Security is configured for HTTP Basic with one hardcoded user. Do not add OAuth2, JWT, or multi-user registration unless explicitly instructed.
8. **CORS is configured in `SecurityConfig`.** The allowed origin is `http://localhost:4200`. Do not add `@CrossOrigin` annotations to controllers.
9. **`application.yml` is the single config file.** Do not create `application.properties` or profile-specific YAML files unless instructed.

### Frontend

1. **Standalone components only.** Every Angular component, directive, and pipe must use `standalone: true`. No `NgModule` declarations.
2. **Angular Material for all UI elements.** Do not introduce third-party component libraries other than ng2-charts. Do not write raw HTML `<input>`, `<button>`, or `<select>` elements where a Material equivalent exists.
3. **Services own HTTP calls.** Components must not inject `HttpClient` directly. All API calls go through a service in `core/services/`.
4. **Models mirror DTOs.** TypeScript interfaces in `core/models/` must match the backend DTO field names exactly (camelCase). Do not invent frontend-only field names for data that comes from the API.
5. **Feature routing is lazy-loaded.** Each feature folder has its own `routes.ts`; the top-level `app.routes.ts` uses `loadChildren` or `loadComponent` to lazy-load features.
6. **No inline styles.** All component-specific styles go in the component's `.scss` file. Global styles go in `src/styles.scss`.
7. **Reactive Forms for any form with validation.** Do not use Template-driven forms.
8. **The dev proxy handles `/api` routing.** `proxy.conf.json` forwards `/api` to `http://localhost:8080`. Do not hardcode backend URLs in services; use `/api/...` paths.

### Database

1. **PostgreSQL 16 is the target database.** Write SQL that is valid for PostgreSQL. Do not write ANSI-generic or MySQL-compatible SQL.
2. **Flyway migration files are immutable once committed.** Never modify an existing `V{n}__` file. Add a new migration to change existing schema.
3. **Enum values stored as strings.** JPA enums use `@Enumerated(EnumType.STRING)`. Never use ordinal storage.

---

## 6. What NOT To Do

- **Do not create new entities or tables** unless the feature spec explicitly names them.
- **Do not add new API endpoints** beyond what a feature spec defines. One feature = the endpoints listed in its spec.
- **Do not modify existing working features** while implementing a new one. Scope changes to the files the spec identifies.
- **Do not use `var` in Java.** Use explicit types for all local variables and method signatures.
- **Do not generate `@Autowired` field injection.** Use constructor injection for all Spring-managed dependencies.
- **Do not generate Lombok annotations** unless Lombok is already present as a `pom.xml` dependency.
- **Do not add `console.log` statements** to Angular components or services in code you generate.
- **Do not generate Angular `NgModule` files.** The project uses standalone components throughout.
- **Do not modify `V1__init_schema.sql` or `V2__seed_questions.sql`.** These are immutable baseline migrations.
- **Do not use `any` in TypeScript.** All variables, parameters, and return types must be explicitly typed.
- **Do not generate README updates, changelog entries, or documentation files** unless asked.
- **Do not introduce AI, LLM, or external API integrations.** The app is entirely self-contained.
- **Do not change `SecurityConfig`** for any feature work. Security configuration is not a feature task.
- **Do not use `ddl-auto: create` or `ddl-auto: update`** in `application.yml`. Schema is managed by Flyway exclusively.

---

## 7. Codebase Navigation — Graphify Knowledge Graph

A pre-built knowledge graph of this repository lives in [`graphify-out/`](graphify-out/). Use it to understand structure and relationships **before** searching or editing — it is faster and more accurate than reading files blindly, and it surfaces cross-layer connections (e.g. which Angular component maps to which DTO and entity).

### When to use it

- Before implementing a feature that touches code you haven't read yet — locate the relevant entities, DTOs, services, and components.
- When you need to know what depends on a class before changing it (impact analysis).
- When you are unsure which file owns a concept — query the graph instead of guessing.

### How to use it

The graph is queryable via the `graphify` CLI (already installed). Run commands from the repo root:

```bash
graphify query "how does the bookmark feature flow from UI to database"   # BFS — broad context across files
graphify query "what depends on QuestionResponse" --dfs                    # DFS — trace one dependency path
graphify path "BookmarksComponent" "Bookmark"                              # shortest path between two concepts
graphify explain "MockSession"                                             # plain-language summary of one node
```

If the CLI is unavailable, read the static artifacts directly:

- [`graphify-out/GRAPH_REPORT.md`](graphify-out/GRAPH_REPORT.md) — god nodes (most-connected core abstractions), community hubs, and surprising cross-layer links. Start here for a map of the codebase.
- [`graphify-out/graph.json`](graphify-out/graph.json) — full nodes/edges for programmatic lookup.
- [`graphify-out/graph.html`](graphify-out/graph.html) — interactive visual graph.

### Rules

- Treat the graph as a **navigation aid, not ground truth** — always confirm against the actual source file before editing.
- The graph is a generated artifact. Do not hand-edit files under `graphify-out/`; regenerate with `graphify . --update` after significant code changes.