package com.interviewbuddy.dto;

import com.interviewbuddy.domain.Bookmark;

import java.time.LocalDateTime;
import java.util.UUID;

public class BookmarkResponse {

    private UUID id;
    private QuestionResponse question;
    private String note;
    private LocalDateTime createdAt;

    public static BookmarkResponse from(Bookmark b) {
        BookmarkResponse dto = new BookmarkResponse();
        dto.setId(b.getId());
        dto.setQuestion(QuestionResponse.from(b.getQuestion()));
        dto.setNote(b.getNote());
        dto.setCreatedAt(b.getCreatedAt());
        return dto;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public QuestionResponse getQuestion() { return question; }
    public void setQuestion(QuestionResponse question) { this.question = question; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
