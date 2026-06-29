package com.interviewbuddy.dto;

import com.interviewbuddy.domain.Bookmark;
import com.interviewbuddy.domain.DifficultyLevel;
import com.interviewbuddy.domain.Topic;

import java.time.LocalDateTime;
import java.util.UUID;

public class BookmarkResponse {

    private UUID id;
    private UUID questionId;
    private String questionText;
    private Topic topic;
    private DifficultyLevel difficultyLevel;
    private String note;
    private LocalDateTime createdAt;

    public static BookmarkResponse from(Bookmark bookmark) {
        BookmarkResponse dto = new BookmarkResponse();
        dto.setId(bookmark.getId());
        dto.setQuestionId(bookmark.getQuestion().getId());
        dto.setQuestionText(bookmark.getQuestion().getTitle());
        dto.setTopic(bookmark.getQuestion().getTopic());
        dto.setDifficultyLevel(bookmark.getQuestion().getDifficultyLevel());
        dto.setNote(bookmark.getNote());
        dto.setCreatedAt(bookmark.getCreatedAt());
        return dto;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getQuestionId() {
        return questionId;
    }

    public void setQuestionId(UUID questionId) {
        this.questionId = questionId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public DifficultyLevel getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(DifficultyLevel difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
