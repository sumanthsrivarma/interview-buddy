package com.interviewbuddy.controller;

import com.interviewbuddy.dto.*;
import com.interviewbuddy.service.MockSessionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/sessions")
public class MockSessionController {

    private final MockSessionService service;

    public MockSessionController(MockSessionService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<SessionResponse>> list() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/history")
    public ResponseEntity<List<SessionResponse>> history() {
        return ResponseEntity.ok(service.findHistory());
    }

    @PostMapping
    public ResponseEntity<SessionDetailResponse> create(@Valid @RequestBody SessionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SessionDetailResponse> get(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<SessionDetailResponse> complete(@PathVariable UUID id) {
        return ResponseEntity.ok(service.complete(id));
    }

    @PutMapping("/{id}/attempts/{aid}")
    public ResponseEntity<AttemptResponse> updateAttempt(
            @PathVariable UUID id,
            @PathVariable UUID aid,
            @RequestBody AttemptRequest request) {
        return ResponseEntity.ok(service.updateAttempt(id, aid, request));
    }

    @GetMapping("/{id}/attempts")
    public ResponseEntity<List<AttemptResponse>> getAttempts(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getAttempts(id));
    }
}
