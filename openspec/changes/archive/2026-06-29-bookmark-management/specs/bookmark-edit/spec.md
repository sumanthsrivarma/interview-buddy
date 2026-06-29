## ADDED Requirements

### Requirement: User can view all bookmarks
The system SHALL display a list of all bookmarked questions showing the question text, topic, difficulty, and any saved personal note.

#### Scenario: Bookmarks list loads
- **WHEN** the user navigates to the bookmarks page
- **THEN** the system SHALL call `GET /api/bookmarks` and render one card per bookmark

#### Scenario: Empty bookmarks list
- **WHEN** the user has no bookmarks
- **THEN** the system SHALL display an empty-state message indicating no bookmarks have been saved

### Requirement: User can edit a bookmark note
The system SHALL allow the user to edit the personal note on any bookmark and persist the change via `PUT /api/bookmarks/{id}/note`.

#### Scenario: Enter edit mode
- **WHEN** the user clicks the edit icon on a bookmark card
- **THEN** the note field SHALL become an editable `<mat-form-field>` pre-populated with the existing note value, and save / cancel buttons SHALL appear

#### Scenario: Save updated note
- **WHEN** the user modifies the note text and clicks save
- **THEN** the system SHALL call `PUT /api/bookmarks/{id}/note` with the new note value
- **THEN** on HTTP 200 the inline editor SHALL close and the updated note SHALL be displayed

#### Scenario: Cancel edit
- **WHEN** the user clicks cancel during edit mode
- **THEN** the note field SHALL revert to its previous value and the inline editor SHALL close without making a network call

#### Scenario: Save empty note
- **WHEN** the user clears the note text and clicks save
- **THEN** the system SHALL send an empty string (or null) as the note value and persist it

### Requirement: User can delete a bookmark
The system SHALL allow the user to delete a bookmark via `DELETE /api/bookmarks/{id}`, removing it from the list.

#### Scenario: Delete bookmark
- **WHEN** the user clicks the delete icon on a bookmark card
- **THEN** the system SHALL call `DELETE /api/bookmarks/{id}`
- **THEN** on HTTP 204 the bookmark card SHALL be removed from the list immediately

#### Scenario: Delete fails
- **WHEN** the `DELETE /api/bookmarks/{id}` call returns an error
- **THEN** the bookmark SHALL remain in the list and the system SHALL display an error snackbar
