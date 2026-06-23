package com.interviewbuddy.controller;

import com.interviewbuddy.domain.DifficultyLevel;
import com.interviewbuddy.domain.TechStack;
import com.interviewbuddy.domain.Topic;
import com.interviewbuddy.dto.QuestionRequest;
import com.interviewbuddy.dto.QuestionResponse;
import com.interviewbuddy.service.QuestionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionService service;

    public QuestionController(QuestionService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<QuestionResponse>> list(
            @RequestParam(required = false) Topic topic,
            @RequestParam(required = false) TechStack techStack,
            @RequestParam(required = false) DifficultyLevel difficultyLevel,
            @RequestParam(defaultValue = "false") boolean includeInactive,
            @PageableDefault(size = 20) Pageable pageable) {

        return ResponseEntity.ok(service.findAll(topic, techStack, difficultyLevel, includeInactive, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionResponse> get(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<QuestionResponse> create(@Valid @RequestBody QuestionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuestionResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody QuestionRequest request) {

        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable UUID id) {
        service.deactivate(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/topics")
    public ResponseEntity<Topic[]> topics() {
        return ResponseEntity.ok(Topic.values());
    }

    @GetMapping("/stacks")
    public ResponseEntity<TechStack[]> stacks() {
        return ResponseEntity.ok(TechStack.values());
    }
}
