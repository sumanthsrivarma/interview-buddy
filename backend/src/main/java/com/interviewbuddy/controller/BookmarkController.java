package com.interviewbuddy.controller;

import com.interviewbuddy.dto.BookmarkRequest;
import com.interviewbuddy.dto.BookmarkResponse;
import com.interviewbuddy.dto.UpdateBookmarkNoteRequest;
import com.interviewbuddy.service.BookmarkService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/bookmarks")
public class BookmarkController {

    private final BookmarkService service;

    public BookmarkController(BookmarkService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<BookmarkResponse>> list() {
        return ResponseEntity.ok(service.listAll());
    }

    @PostMapping
    public ResponseEntity<BookmarkResponse> create(@RequestBody BookmarkRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}/note")
    public ResponseEntity<BookmarkResponse> updateNote(
            @PathVariable UUID id,
            @RequestBody UpdateBookmarkNoteRequest request) {
        return ResponseEntity.ok(service.updateNote(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
