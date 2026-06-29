---
applyTo: "backend/src/test/java/**/*.java"
---

# Java Test Instructions — InterviewBuddy

## 1. Scope

This file applies exclusively to test classes under `backend/src/test/java/`. It does not govern production code — see `java-instructions.md` for production rules. All tests are written with JUnit 5, Mockito, and AssertJ. These are provided transitively by `spring-boot-starter-test`; do not add them as explicit `pom.xml` dependencies.

---

## 2. Test Type Decision

Choose the narrowest test type that proves the behaviour. Work down this list in order and stop at the first match.

### `@ExtendWith(MockitoExtension.class)` — Pure unit test

Use for: service classes, DTO factory methods, any class whose dependencies can be replaced with mocks.

This is the default choice for all service tests. It starts fast, requires no Spring context, and tests logic in isolation. It covers the vast majority of new test work.

```java
@ExtendWith(MockitoExtension.class)
class BookmarkServiceTest { ... }
```

---

### `@WebMvcTest(XyzController.class)` — Controller slice test

Use for: verifying HTTP binding, request validation, response status codes, and URL mappings. The slice starts only the web layer — no service beans, no database.

Mock all service dependencies with `@MockBean`. Annotate tests with `@WithMockUser` to satisfy Spring Security's authentication requirement. Do not use `@WebMvcTest` to test business logic — that belongs in a service unit test.

```java
@WebMvcTest(BookmarkController.class)
class BookmarkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookmarkService service;

    @Test
    @WithMockUser
    void shouldReturn200_whenNoteUpdatedSuccessfully() throws Exception { ... }
}
```

---

### `@DataJpaTest` — Repository slice test

Use for: custom `@Query` methods, derived query methods with non-trivial logic, or `JpaSpecificationExecutor` filtering. The slice starts only JPA infrastructure. H2 is the in-memory database used at test scope.

Do not use `@DataJpaTest` just to verify that Spring Data's built-in `findById` or `save` work — those are framework internals, not your code.

```java
@DataJpaTest
class QuestionRepositoryTest { ... }
```

---

### `@SpringBootTest` — Full integration test

Use as a last resort only. Valid use cases:
- The `contextLoads` smoke test in `InterviewBuddyApplicationTests`
- End-to-end flows that cannot be covered by slices (rare in a single-module app)

Never use `@SpringBootTest` for testing a single service or repository. It loads the entire application context, requires a running database or complex test configuration, and is orders of magnitude slower than a unit test.

When `@SpringBootTest` is genuinely required, add `@ActiveProfiles("test")` and configure H2 as the datasource in `application-test.yml`.

---

## 3. Test Class Structure

### File placement

Mirror the production package in the test tree. A test for `com.interviewbuddy.service.BookmarkService` lives at `com.interviewbuddy.service.BookmarkServiceTest`.

### Class structure

Organise every test class in this order:

1. Test doubles (`@Mock`, `@MockBean`, `@Autowired` slice beans)
2. Subject under test (`@InjectMocks` or `@Autowired`)
3. Shared fixtures declared as fields
4. `@BeforeEach` setup method — build reusable entity/DTO objects
5. Test methods grouped by the method they exercise, separated by a blank line

```java
@ExtendWith(MockitoExtension.class)
class BookmarkServiceTest {

    @Mock
    private BookmarkRepository repository;

    @InjectMocks
    private BookmarkService service;

    private UUID bookmarkId;
    private Bookmark bookmark;

    @BeforeEach
    void setUp() {
        bookmarkId = UUID.randomUUID();
        bookmark = new Bookmark();
        bookmark.setId(bookmarkId);
        // ... set remaining fields
    }

    // test methods below
}
```

### Assertion style

Use **AssertJ** (`org.assertj.core.api.Assertions`) for all assertions. Do not use JUnit's `assertEquals`, `assertTrue`, `assertNull`, or `assertThrows`. AssertJ provides fluent, readable failure messages.

```java
// Correct — AssertJ
assertThat(result.getId()).isEqualTo(bookmarkId);
assertThat(result.getNote()).isEqualTo("revisit this topic");
assertThat(results).hasSize(2).extracting(BookmarkResponse::getId).containsExactlyInAnyOrder(id1, id2);

// Wrong — JUnit assertions
assertEquals(bookmarkId, result.getId());
assertNull(result.getNote());
```

For exception assertions, use `assertThatThrownBy`:

```java
assertThatThrownBy(() -> service.updateNote(unknownId, request))
        .isInstanceOf(ResponseStatusException.class)
        .satisfies(ex -> assertThat(((ResponseStatusException) ex).getStatusCode())
                .isEqualTo(HttpStatus.NOT_FOUND));
```

---

## 4. Test Method Naming

Use the pattern `shouldDoSomething_whenCondition`. The name must be a sentence that describes the expected outcome and the condition that produces it. Read it like a specification — someone should understand what the test proves without reading the body.

```java
// Correct
void shouldReturnBookmarkResponse_whenBookmarkExists()
void shouldThrowNotFound_whenBookmarkIdDoesNotExist()
void shouldPersistUpdatedNote_whenNoteRequestIsValid()
void shouldReturnEmptyList_whenNoBookmarksExist()
void shouldThrowConflict_whenQuestionAlreadyBookmarked()

// Wrong — these names say nothing about behaviour
void testBookmark()
void test1()
void updateNoteTest()
void bookmarkNotFound()
```

The `whenCondition` suffix may be omitted only when the condition is the obvious happy path and there is no ambiguity.

---

## 5. Mocking Rules

### `@Mock` and `@InjectMocks`

Use `@Mock` to create test doubles for all dependencies of the class under test. Use `@InjectMocks` to instantiate the class under test with those mocks injected via its constructor.

```java
@Mock
private BookmarkRepository repository;

@Mock
private QuestionRepository questionRepository;

@InjectMocks
private BookmarkService service;  // constructed with both mocks
```

**Never apply `@Mock` to the class under test itself.** Mocking the subject defeats the purpose of the test.

### When to stub vs when to use a real object

- **Stub** (`when(...).thenReturn(...)`) any dependency that crosses a boundary: repositories, other services, or any class with side effects.
- **Use the real object** for value objects, DTOs, entities, and enums — constructing them directly is clearer and faster than mocking them.
- **Do not stub** methods that are not called by the code path under test. Unnecessary stubbing is noise; Mockito's strict stubbing mode (enabled by `MockitoExtension`) will fail the test if a stub is declared but never invoked.

```java
// Correct — construct the entity directly
Question question = new Question();
question.setId(UUID.randomUUID());
question.setTitle("Explain volatile in Java");
question.setTopic(Topic.CONCURRENCY);

// Wrong — never mock an entity or DTO
Question question = mock(Question.class);
when(question.getId()).thenReturn(someId);
```

### Verifying interactions

Use `verify()` only when a void side-effect method (e.g. `repository.delete(...)`, `repository.save(...)`) is the key behaviour being tested. Do not verify every repository call on every test — focus on the outcome, not the implementation.

---

## 6. Coverage Expectations

Every service method must have at minimum:

| Scenario | What to assert |
|---|---|
| **Happy path** | Returned DTO fields match the entity state used to set up the mock |
| **Not found / null input** | `ResponseStatusException` is thrown with the correct `HttpStatus` |
| **Boundary / business rule violation** | The appropriate `HttpStatus` (e.g. `BAD_REQUEST`, `CONFLICT`) and a non-null message |
| **Write method persistence** | The entity passed to `repository.save()` has the expected field values, captured with `ArgumentCaptor` |

An `ArgumentCaptor` example for verifying persistence:

```java
@Test
void shouldPersistUpdatedNote_whenNoteRequestIsValid() {
    NoteRequest request = new NoteRequest();
    request.setNote("revisit this topic");
    when(repository.findById(bookmarkId)).thenReturn(Optional.of(bookmark));
    when(repository.save(any(Bookmark.class))).thenAnswer(inv -> inv.getArgument(0));

    service.updateNote(bookmarkId, request);

    ArgumentCaptor<Bookmark> captor = ArgumentCaptor.forClass(Bookmark.class);
    verify(repository).save(captor.capture());
    assertThat(captor.getValue().getNote()).isEqualTo("revisit this topic");
}
```

---

## 7. What NOT To Do

- **Do not use `@SpringBootTest` for unit tests.** If you are testing a service method, use `@ExtendWith(MockitoExtension.class)`. `@SpringBootTest` is reserved for context-load verification and true integration tests.
- **Do not use `@Autowired` to inject the subject under test in a unit test.** Use `@InjectMocks`. `@Autowired` requires a Spring context; `@InjectMocks` does not.
- **Do not name test methods `test1()`, `testBookmark()`, or `testService()`.** Every method name must describe the expected outcome and the condition.
- **Do not assert on `toString()` output.** Assert on individual fields using AssertJ's typed accessors.
- **Do not share mutable fixture state between test methods.** Reinitialise all entities in `@BeforeEach`. Test methods must be independent and runnable in any order.
- **Do not use `@Autowired` field injection in test classes.** For `@WebMvcTest` and `@DataJpaTest`, use `@Autowired` constructor injection or `@Autowired` field injection is acceptable only where the slice annotation requires it (e.g. `MockMvc`). For pure Mockito tests, `@InjectMocks` handles wiring.
- **Do not catch expected exceptions with try/catch in tests.** Use `assertThatThrownBy` instead.
- **Do not test DTO getters and setters.** These are trivial Java boilerplate. Test the `static from(Entity)` factory method by asserting on the mapped field values.
- **Do not use `Thread.sleep()` or time-dependent assertions.** If a method sets `LocalDateTime.now()` internally (e.g. `completedAt`), assert that the value `isNotNull()` and `isBeforeOrEqualTo(LocalDateTime.now())` rather than asserting an exact timestamp.

---

## 8. Complete Example — Service Unit Test

The following test class covers `QuestionService` following every rule in this file. Use it as the reference pattern for all new service test classes.

```java
package com.interviewbuddy.service;

import com.interviewbuddy.domain.DifficultyLevel;
import com.interviewbuddy.domain.Question;
import com.interviewbuddy.domain.TechStack;
import com.interviewbuddy.domain.Topic;
import com.interviewbuddy.dto.QuestionResponse;
import com.interviewbuddy.repository.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {

    @Mock
    private QuestionRepository repository;

    // No @InjectMocks here because QuestionService is instantiated directly below.
    // Either approach is acceptable; direct instantiation makes the constructor call explicit.
    private QuestionService service;

    private UUID questionId;
    private Question question;

    @BeforeEach
    void setUp() {
        service = new QuestionService(repository);

        questionId = UUID.randomUUID();

        question = new Question();
        question.setId(questionId);
        question.setTitle("What is the difference between HashMap and ConcurrentHashMap?");
        question.setBody("Explain thread-safety implications.");
        question.setTopic(Topic.COLLECTIONS);
        question.setTechStack(TechStack.JAVA);
        question.setDifficultyLevel(DifficultyLevel.MEDIUM);
        question.setActive(true);
    }

    // --- findById ---

    @Test
    void shouldReturnQuestionResponse_whenQuestionExists() {
        when(repository.findById(questionId)).thenReturn(Optional.of(question));

        QuestionResponse result = service.findById(questionId);

        assertThat(result.getId()).isEqualTo(questionId);
        assertThat(result.getTitle()).isEqualTo("What is the difference between HashMap and ConcurrentHashMap?");
        assertThat(result.getTopic()).isEqualTo(Topic.COLLECTIONS);
        assertThat(result.getDifficultyLevel()).isEqualTo(DifficultyLevel.MEDIUM);
        assertThat(result.isActive()).isTrue();
    }

    @Test
    void shouldThrowNotFound_whenQuestionIdDoesNotExist() {
        UUID unknownId = UUID.randomUUID();
        when(repository.findById(unknownId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(unknownId))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(ex -> assertThat(((ResponseStatusException) ex).getStatusCode())
                        .isEqualTo(HttpStatus.NOT_FOUND));
    }

    // --- findAll ---

    @Test
    void shouldReturnPagedResponses_whenQuestionsExist() {
        Page<Question> page = new PageImpl<>(List.of(question));
        when(repository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(page);

        Page<QuestionResponse> result = service.findAll(null, null, null, false, PageRequest.of(0, 20));

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getId()).isEqualTo(questionId);
    }

    @Test
    void shouldReturnEmptyPage_whenNoQuestionsMatchFilter() {
        when(repository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(Page.empty());

        Page<QuestionResponse> result = service.findAll(Topic.CONCURRENCY, TechStack.JAVA, DifficultyLevel.HARD, false, PageRequest.of(0, 20));

        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isZero();
    }
}
```
