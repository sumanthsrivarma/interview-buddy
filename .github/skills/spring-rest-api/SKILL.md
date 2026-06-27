---
name: spring-rest-api
description: Generate a Spring Boot REST endpoint with service, controller, and DTOs
---

# Skill: Spring REST API

## When to use
When adding a new REST endpoint that requires a service, controller, and DTOs.
Do not use for repository-only changes or entity modifications alone.

## Files this skill creates
- `{Name}Service.java` — interface
- `{Name}ServiceImpl.java` — implementation
- `{Name}Controller.java` — REST controller
- `{Name}Request.java` — request DTO
- `{Name}Response.java` — response DTO (Java record)

## Rules

**Service**
- Constructor injection — fields are `final`
- Business logic lives here only — never in controller
- Throw `ResourceNotFoundException` when entity not found — never return null
- Annotate with `@Service`

**Controller**
- Annotate with `@RestController` and `@RequestMapping("/api/{resource}")`
- Inject service only — nothing else
- One line per method body — delegate entirely to service
- Return `ResponseEntity<{Name}Response>`

**DTOs**
- Response is a Java record with a static `from(Entity e)` factory method
- Request is a class with `@NotNull` / `@NotBlank` on required fields
- Never expose JPA entity in response

**What NOT to do**
- No `@Autowired` field injection
- No `null` returns from service methods
- No entity returned from controller
- No business logic in controller method body

## Pattern

```java
// ServiceImpl — method pattern
@Override
public BookmarkResponse createBookmark(UUID questionId) {
    Question question = questionRepository.findById(questionId)
        .orElseThrow(() -> new ResourceNotFoundException("Question", questionId));
    return BookmarkResponse.from(bookmarkRepository.save(new Bookmark(question)));
}

// Controller — method pattern
@PostMapping
public ResponseEntity<BookmarkResponse> create(@RequestBody @Valid BookmarkRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(bookmarkService.createBookmark(request.questionId()));
}

// Response DTO — record pattern
public record BookmarkResponse(UUID id, UUID questionId, String questionTitle, LocalDateTime createdAt) {
    public static BookmarkResponse from(Bookmark b) {
        return new BookmarkResponse(b.getId(), b.getQuestion().getId(), b.getQuestion().getTitle(), b.getCreatedAt());
    }
}
```

## Checklist
- [ ] All injected fields are `final` with constructor injection
- [ ] No JPA entity returned from controller
- [ ] Response DTO has `from()` factory method
- [ ] Service throws `ResourceNotFoundException` — not returning null
- [ ] Controller method body is one line