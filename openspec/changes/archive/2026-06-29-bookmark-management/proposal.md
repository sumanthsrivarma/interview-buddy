## Why

Users can currently bookmark questions but have no way to edit their bookmark notes, add personal notes after the fact, or delete bookmarks they no longer need. This makes the bookmarks feature incomplete and limits its usefulness as a personal study reference.

## What Changes

- Add ability to edit the personal note on an existing bookmark
- Add ability to delete a bookmark
- Update the bookmarks list UI to expose edit and delete actions per bookmark
- Backend: new `PUT /api/bookmarks/{id}` endpoint to update the note on a bookmark
- Backend: new `DELETE /api/bookmarks/{id}` endpoint to remove a bookmark

## Capabilities

### New Capabilities

- `bookmark-edit`: Edit the personal note on an existing bookmark and save it back to the server
- `bookmark-delete`: Delete a bookmark, removing it from the user's saved list

### Modified Capabilities

- None

## Impact

- **Backend**: `BookmarkController`, `BookmarkService`, `BookmarkRepository` — new update and delete operations
- **Backend DTOs**: new `UpdateBookmarkRequest` DTO for the PUT endpoint
- **Frontend**: `bookmarks` feature component, `BookmarkService` — add `updateBookmark()` and `deleteBookmark()` methods; UI changes to the bookmarks list for inline edit and delete actions
- **No schema changes required**: the `bookmarks` table already has a `note` column
