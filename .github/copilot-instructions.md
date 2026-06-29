# InterviewBuddy — Copilot Workspace Instructions

## Project Identity

InterviewBuddy is a single-user interview preparation tool: question bank, mock sessions, bookmarks, and a progress dashboard. It is not a multi-user platform, not an AI evaluator, and not a job board — do not introduce features outside that scope.

## Tech Stack

- **Backend:** Java 17, Spring Boot 3.x, Spring Data JPA, Spring Security (basic auth)
- **Frontend:** Angular 20, Angular Material, standalone components
- **Database:** PostgreSQL 16 (Docker), schema managed exclusively by Flyway
- **Tests:** JUnit 5 + Mockito + AssertJ (backend), H2 in-memory (test scope only)

## Always Read First

Before starting any task, read [`AGENTS.md`](../AGENTS.md) in the project root. It defines the full domain vocabulary, architectural constraints, and explicit anti-patterns. Do not skip it.

## Layer Routing

| When working on… | Also read… |
|---|---|
| Any file under `backend/src/main/java/` | `.github/instructions/java-instructions.md` |
| Any file under `backend/src/test/java/` | `.github/instructions/java-test-instructions.md` |
| Any file under `frontend/src/` | `.github/instructions/angular-instructions.md` |
| A Flyway migration (`db/migration/`) | `.github/instructions/database-migrations.instructions.md` |

## Global Rules

These apply to every file in every layer, without exception:

1. **No secrets in code.** Never hardcode passwords, tokens, API keys, or credentials. Configuration values live in `application.yml` and are never committed as literals in source files.
2. **No debug output in production code.** No `System.out.println`, `console.log`, or logging statements added solely for debugging. Use the configured logger if a log statement is genuinely needed.
3. **Follow naming conventions.** Every new file must follow the naming rules in the relevant layer instruction file. Do not invent names that differ from the established patterns.
4. **Flyway migration files are immutable.** Never edit `V1__init_schema.sql`, `V2__seed_questions.sql`, or any existing `V{n}__` file. Schema changes require a new migration file.

## When in Doubt

Stop and ask. Do not guess at scope, invent new abstractions, or add dependencies to resolve ambiguity. A clarifying question is always better than an incorrect assumption baked into generated code.
