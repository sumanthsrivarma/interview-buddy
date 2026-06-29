## Context

The `bookmarks` feature stores user-saved questions with optional personal notes. The frontend `BookmarkService` already defines `delete()` and `updateNote()` methods, and the `Bookmark` / `BookmarkNoteRequest` models are in place. The backend has no `Bookmark*` classes yet (no controller, service, or repository). The bookmarks UI is a "coming soon" placeholder component. No schema changes are needed — the `bookmarks` table already has a `note` column.

## Goals / Non-Goals

**Goals:**
- Implement the full bookmarks list UI (show all bookmarks, question text, topic, difficulty, and personal note)
- Allow the user to edit a bookmark's note inline and save it
- Allow the user to delete a bookmark from the list
- Implement backend `PUT /api/bookmarks/{id}/note` and `DELETE /api/bookmarks/{id}` endpoints

**Non-Goals:**
- Creating new bookmarks (that happens from the question detail view — out of scope here)
- Paginating the bookmarks list
- Sorting or filtering bookmarks
- AI-generated note suggestions

## Decisions

### Decision 1: Reuse existing `BookmarkService` frontend methods
The frontend service already exposes `delete(id)` and `updateNote(id, request)`. The component simply injects the service and calls those methods directly.

*Alternative*: Rebuild the service from scratch — rejected; existing shape is correct.

### Decision 2: Inline editing with a toggle
Each bookmark row has an "edit" icon button that switches the note field to an editable `<mat-form-field>`. Saving triggers the PUT call; cancelling restores the previous value.

*Alternative*: Open a dialog for editing — rejected; a full dialog is heavy for a single text field.

### Decision 3: `PUT /api/bookmarks/{id}/note` (not a generic `PUT /api/bookmarks/{id}`)
Only the note is editable. Using a dedicated `/note` sub-resource makes the intent explicit and avoids accepting full bookmark objects on the wire.

*Alternative*: Generic `PATCH /api/bookmarks/{id}` — adds complexity with no benefit here.

### Decision 4: `DELETE /api/bookmarks/{id}` returns `204 No Content`
Standard REST convention for delete; the frontend removes the item from the local list on success.

## Risks / Trade-offs

- **Risk**: User edits note and navigates away without saving → Mitigation: save button is explicit; no auto-save.
- **Risk**: Stale list if delete fails silently → Mitigation: handle error in component; show snackbar on failure; do not remove item from list until HTTP 204 received.
