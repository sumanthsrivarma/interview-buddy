---
applyTo: "backend/src/main/java/**/*.java"
---

# Java Backend Instructions — InterviewBuddy

## 1. Dependency Injection

Use **constructor injection only**. Never use `@Autowired` field injection or setter injection. Declare all dependencies as `private final` fields assigned in the constructor. The `@Autowired` annotation is not required when there is exactly one constructor.

---

## 2. Layering Rules

### Controller (`com.interviewbuddy.controller`)

- Annotate with `@RestController` and `@RequestMapping("/api/...")`
- Return `ResponseEntity<T>` on every handler method
- Use `@Valid` on `@RequestBody` when the DTO carries bean validation annotations
- Use `@PathVariable UUID id` for resource identifiers
- Call exactly one service method per handler — no repository calls, no branching on business rules, no entity-to-DTO mapping, no exception catching

### Service (`com.interviewbuddy.service`)

- Annotate with `@Service` and `@Transactional(readOnly = true)`; override individual write methods with `@Transactional`
- Accept and return DTOs — never expose JPA entities across the public API boundary
- Map entities to response DTOs using the DTO's static `from()` factory method
- Throw `ResponseStatusException` with the appropriate `HttpStatus` for not-found and business rule violations
- No HTTP response construction, no `HttpServletRequest`/`HttpServletResponse`, no manual commit/rollback

### Repository (`com.interviewbuddy.repository`)

- Extend `JpaRepository<Entity, UUID>`; add `JpaSpecificationExecutor<Entity>` for dynamic filtering
- Use derived query methods or `@Query` JPQL — no business logic, no data transformation
- Native SQL only when JPQL cannot express the query; document the reason in a comment above the `@Query`

---

## 3. Entity Rules

Place entities in `com.interviewbuddy.domain`.

- **Primary keys:** `UUID` with `@GeneratedValue(strategy = GenerationType.UUID)` — never auto-increment integers
- **Enums:** `@Enumerated(EnumType.STRING)` — never ordinal
- **Timestamps:** `@PrePersist` / `@PreUpdate` lifecycle callbacks — do not use `@EnableJpaAuditing`
- **Column mapping:** `@Column(nullable = false)` for non-optional fields; `columnDefinition = "TEXT"` for unbounded text; explicit `name = "snake_case_name"` when the Java field name differs from the column name
- **Soft delete:** `Question` only — set `isActive = false`; never issue `DELETE` on a `Question` row

---

## 4. DTO Rules

Place all DTOs in `com.interviewbuddy.dto`. Separate request and response types — never reuse a DTO for both directions.

- **Request DTOs** — Java records with bean validation annotations (`@NotBlank`, `@NotNull`, `@Size`) on record components
- **Response DTOs** — Java records with a `public static T from(Entity entity)` factory method inside the record body; never plain classes with getters/setters

---

## 5. Exception Handling

Throw `ResponseStatusException` from the service layer — Spring MVC produces the HTTP error automatically. Do not catch it in controllers. Never swallow exceptions or return `null` where an exception should be thrown.

- `NOT_FOUND (404)` — resource does not exist
- `BAD_REQUEST (400)` — invalid input that passes bean validation but violates a business rule
- `CONFLICT (409)` — duplicate resource (e.g. bookmarking the same question twice)

---

## 6. Naming Conventions

| Type | Pattern | Example |
|---|---|---|
| Entity | `PascalCase` noun | `MockSession`, `SessionAttempt` |
| Controller | `{Entity}Controller` | `BookmarkController` |
| Service | `{Entity}Service` | `BookmarkService` |
| Repository | `{Entity}Repository` | `BookmarkRepository` |
| Request DTO | `{Action}Request` | `NoteRequest`, `SessionRequest` |
| Response DTO | `{Entity}Response` | `BookmarkResponse` |

- Service query methods: `findById`, `findAll`, `findBy{Criteria}`
- Service write methods: `create`, `update`, `complete`, `deactivate`
- DTO factory method: always `from(Entity entity)` — no other name
- Repository derived methods: `findBy`, `existsBy`, `countBy`
- Sub-packages are lowercase single words; no nested packages within a layer
- No `var` — explicit types everywhere; controller path variables are `id`, service parameters use descriptive names (`bookmarkId`)

---

## 7. What NOT To Do

- No `@Autowired` field injection — constructor injection only
- No `var` — explicit types on all local variables and parameters
- No business logic in controllers — no `if`/`switch` on domain rules, no direct repository calls
- No JPA entities returned from controllers — handlers return a DTO inside `ResponseEntity`
- No plain-class DTOs — records only
- No Lombok — not a project dependency; records remove the need
- No `@CrossOrigin` on controllers — CORS is configured globally in `SecurityConfig`
- No `ddl-auto: create/update` in `application.yml` — Flyway manages schema; use `validate`
- No `application.properties` or profile YAML files — `application.yml` is the only config file
- No new Maven dependencies without explicit instruction
- No native SQL `@Query` without a documented reason
- No changes to `SecurityConfig` for feature work — HTTP Basic with one hardcoded user; do not introduce OAuth2 or JWT
