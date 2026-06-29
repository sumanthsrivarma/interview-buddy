## 1. Backend — Domain & Repository

- [x] 1.1 Create `Bookmark` JPA entity in `domain/` with fields: `id` (UUID), `question` (ManyToOne), `note` (String), `createdAt` (LocalDateTime)
- [x] 1.2 Create `BookmarkRepository` extending `JpaRepository<Bookmark, UUID>` with `findAllByOrderByCreatedAtDesc()`
- [x] 1.3 Add `@Enumerated(EnumType.STRING)` check — ensure `Question` relation is correctly mapped

## 2. Backend — DTOs

- [x] 2.1 Create `BookmarkResponse` DTO (id, questionId, questionText, topic, difficultyLevel, note, createdAt)
- [x] 2.2 Create `BookmarkRequest` DTO (questionId)
- [x] 2.3 Create `UpdateBookmarkNoteRequest` DTO (note)

## 3. Backend — Service

- [x] 3.1 Create `BookmarkService` with `listAll()` → `List<BookmarkResponse>`
- [x] 3.2 Add `create(BookmarkRequest)` → `BookmarkResponse`
- [x] 3.3 Add `updateNote(UUID id, UpdateBookmarkNoteRequest)` → `BookmarkResponse` (throws 404 if not found)
- [x] 3.4 Add `delete(UUID id)` (throws 404 if not found)

## 4. Backend — Controller

- [x] 4.1 Create `BookmarkController` with `GET /api/bookmarks` → `ResponseEntity<List<BookmarkResponse>>`
- [x] 4.2 Add `POST /api/bookmarks` → `ResponseEntity<BookmarkResponse>` (status 201)
- [x] 4.3 Add `PUT /api/bookmarks/{id}/note` → `ResponseEntity<BookmarkResponse>`
- [x] 4.4 Add `DELETE /api/bookmarks/{id}` → `ResponseEntity<Void>` (status 204)

## 5. Backend — Tests

- [x] 5.1 Write unit tests for `BookmarkService` covering list, updateNote (happy path + 404), delete (happy path + 404)

## 6. Frontend — Model & Service

- [x] 6.1 Verify `Bookmark`, `BookmarkRequest`, `BookmarkNoteRequest` interfaces in `bookmark.model.ts` match the backend `BookmarkResponse` fields (update if needed)
- [x] 6.2 Verify `BookmarkService.list()`, `updateNote()`, `delete()` methods are complete and correct

## 7. Frontend — Bookmarks Component

- [x] 7.1 Inject `BookmarkService` into `BookmarksComponent` and load bookmarks on init via `ngOnInit`
- [x] 7.2 Render bookmarks list: one `<mat-card>` per bookmark showing question text, topic chip, difficulty badge, and note
- [x] 7.3 Add delete icon button per card; on click call `BookmarkService.delete(id)`, remove card from list on success, show error snackbar on failure
- [x] 7.4 Add edit icon button per card; on click toggle inline edit mode showing a `<mat-form-field>` for the note
- [x] 7.5 Add save and cancel buttons in edit mode; save calls `BookmarkService.updateNote(id, request)`, updates displayed note and exits edit mode; cancel restores original note and exits edit mode
- [x] 7.6 Handle empty state: display a message when the bookmarks list is empty
- [x] 7.7 Add `MatSnackBarModule` import and wire error snackbar for delete failures

## 8. Frontend — Styles

- [x] 8.1 Add component styles in `bookmarks.component.scss` for card layout, note display, inline edit form, and action buttons
