# InterviewBuddy — Copilot Workspace Instructions

## Project Identity

InterviewBuddy is a single-user interview preparation tool: question bank, mock sessions, bookmarks, and a progress dashboard. It is not a multi-user platform, not an AI evaluator, and not a job board — do not introduce features outside that scope.

## Always Read First

Before starting any task, read [`AGENTS.md`](../AGENTS.md) in the project root. It defines the full domain vocabulary, tech stack, repo structure, and project scope. Do not skip it.

## Layer Routing

| When working on… | Also read… |
|---|---|
| Any file under `backend/src/main/java/` | `.github/instructions/java.instructions.md` |
| Any file under `backend/src/test/java/` | `.github/instructions/java-test.instructions.md` |
| Any file under `frontend/src/` | `.github/instructions/angular.instructions.md` |

## Global Rules

1. **No secrets in code.** Never hardcode passwords, tokens, API keys, or credentials. Configuration values live in `application.yml`.
2. **No debug output in production code.** No `System.out.println` or `console.log` — use the configured logger only when a log statement is genuinely needed.
3. **Flyway migration files are immutable.** Never edit any existing `V{n}__` file. Schema changes require a new migration file.

## When in Doubt

Stop and ask. Do not guess at scope, invent new abstractions, or add dependencies to resolve ambiguity.
