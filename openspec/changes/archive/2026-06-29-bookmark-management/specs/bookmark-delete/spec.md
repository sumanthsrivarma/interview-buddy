## ADDED Requirements

### Requirement: Backend exposes update-note endpoint
The system SHALL expose `PUT /api/bookmarks/{id}/note` accepting `{ "note": "<text>" }` and returning the updated `BookmarkResponse`.

#### Scenario: Update note on existing bookmark
- **WHEN** a PUT request is made to `/api/bookmarks/{id}/note` with a valid note body
- **THEN** the system SHALL update the `note` field on the bookmark and return HTTP 200 with the full bookmark response

#### Scenario: Bookmark not found
- **WHEN** a PUT request is made for a non-existent bookmark ID
- **THEN** the system SHALL return HTTP 404

### Requirement: Backend exposes delete-bookmark endpoint
The system SHALL expose `DELETE /api/bookmarks/{id}` that permanently removes the bookmark row and returns HTTP 204.

#### Scenario: Delete existing bookmark
- **WHEN** a DELETE request is made to `/api/bookmarks/{id}` for an existing bookmark
- **THEN** the system SHALL delete the row and return HTTP 204 with no response body

#### Scenario: Delete non-existent bookmark
- **WHEN** a DELETE request is made for a non-existent bookmark ID
- **THEN** the system SHALL return HTTP 404

### Requirement: Backend exposes list-bookmarks endpoint
The system SHALL expose `GET /api/bookmarks` returning all bookmarks as an array of `BookmarkResponse` objects ordered by creation date descending.

#### Scenario: List bookmarks
- **WHEN** a GET request is made to `/api/bookmarks`
- **THEN** the system SHALL return HTTP 200 with a JSON array of all bookmark responses

#### Scenario: No bookmarks exist
- **WHEN** a GET request is made and there are no bookmarks
- **THEN** the system SHALL return HTTP 200 with an empty JSON array
