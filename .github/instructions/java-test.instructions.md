---
applyTo: "backend/src/test/java/**/*.java"
---

# Java Test Instructions — InterviewBuddy

Test library: JUnit 5 + Mockito + AssertJ, provided transitively by `spring-boot-starter-test`. Do not add them explicitly to `pom.xml`.

---

## 1. Test Type — Pick the Narrowest Fit

| Annotation | Use when | Notes |
|---|---|---|
| `@ExtendWith(MockitoExtension.class)` | Service classes, DTO factories — **default choice** | No Spring context; fast |
| `@WebMvcTest(XyzController.class)` | HTTP binding, status codes, URL mapping | Mock services with `@MockBean`; add `@WithMockUser` |
| `@DataJpaTest` | Custom `@Query`, derived queries, `JpaSpecificationExecutor` | H2 in-memory; don't test Spring Data built-ins |
| `@SpringBootTest` | Context-load smoke test only (`contextLoads`) | Last resort; add `@ActiveProfiles("test")` |

---

## 2. Test Class Structure

Mirror the production package: `com.interviewbuddy.service.BookmarkServiceTest` tests `BookmarkService`.

Order within the class:
1. `@Mock` / `@MockBean` / `@Autowired` fields
2. `@InjectMocks` (or direct instantiation) of the subject
3. Shared fixture fields
4. `@BeforeEach` — build reusable entities/DTOs; reinitialise every time (test methods must be independent)
5. Test methods grouped by the method they exercise

---

## 3. Assertions

Use **AssertJ** for all assertions. Do not use JUnit 5's `assertEquals`, `assertTrue`, `assertNull`, or `assertThrows`.

- Field assertions: `assertThat(result.getId()).isEqualTo(expected)`
- Exception assertions: `assertThatThrownBy(() -> service.method(...)).isInstanceOf(ResponseStatusException.class)`
- Timestamps set internally: assert `isNotNull()` and `isBeforeOrEqualTo(LocalDateTime.now())` — never assert an exact value

---

## 4. Test Method Naming

Pattern: `shouldDoSomething_whenCondition`

Examples: `shouldReturnBookmarkResponse_whenBookmarkExists`, `shouldThrowNotFound_whenBookmarkIdDoesNotExist`, `shouldThrowConflict_whenQuestionAlreadyBookmarked`

`whenCondition` may be omitted for the obvious happy path. Never use `test1()`, `testBookmark()`, or similar.

---

## 5. Mocking Rules

- `@Mock` for all dependencies; `@InjectMocks` for the class under test — never mock the subject itself
- **Stub** (`when(...).thenReturn(...)`) repositories, services, and anything with side effects
- **Construct directly** value objects, entities, and DTOs — do not mock them
- **Verify** (`verify()`) only void side-effect methods (`save`, `delete`) when persistence is the key behaviour; use `ArgumentCaptor` to assert saved field values
- Do not declare stubs that are never invoked — `MockitoExtension` strict mode will fail the test

---

## 6. Coverage Expectations

Every service method needs at minimum:

| Scenario | Assert |
|---|---|
| Happy path | DTO fields match entity state |
| Not found | `ResponseStatusException` with `NOT_FOUND` |
| Business rule violation | Correct `HttpStatus` (`BAD_REQUEST` / `CONFLICT`) and non-null message |
| Write path | `ArgumentCaptor` confirms entity fields passed to `repository.save()` |

---

## 7. What NOT To Do

- No `@SpringBootTest` for service or repository tests — use Mockito or slice annotations
- No `@Autowired` on the subject in a pure unit test — use `@InjectMocks`
- No `toString()` assertions — assert individual fields
- No try/catch for expected exceptions — use `assertThatThrownBy`
- No mutable fixture state shared between tests — reinitialise in `@BeforeEach`
- No tests for DTO getters/setters — test the `from(Entity)` factory method instead
- No `Thread.sleep()` or exact timestamp assertions
