package com.interviewbuddy.dto;

import com.interviewbuddy.domain.Confidence;
import com.interviewbuddy.domain.SessionAttempt;

import java.time.LocalDateTime;
import java.util.UUID;

public class AttemptResponse {

    private UUID id;
    private UUID sessionId;
    private QuestionResponse question;
    private Confidence confidence;
    private String personalNote;
    private LocalDateTime attemptedAt;

    public static AttemptResponse from(SessionAttempt sa) {
        AttemptResponse dto = new AttemptResponse();
        dto.setId(sa.getId());
        dto.setSessionId(sa.getSession().getId());
        dto.setQuestion(QuestionResponse.from(sa.getQuestion()));
        dto.setConfidence(sa.getConfidence());
        dto.setPersonalNote(sa.getPersonalNote());
        dto.setAttemptedAt(sa.getAttemptedAt());
        return dto;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getSessionId() { return sessionId; }
    public void setSessionId(UUID sessionId) { this.sessionId = sessionId; }

    public QuestionResponse getQuestion() { return question; }
    public void setQuestion(QuestionResponse question) { this.question = question; }

    public Confidence getConfidence() { return confidence; }
    public void setConfidence(Confidence confidence) { this.confidence = confidence; }

    public String getPersonalNote() { return personalNote; }
    public void setPersonalNote(String personalNote) { this.personalNote = personalNote; }

    public LocalDateTime getAttemptedAt() { return attemptedAt; }
    public void setAttemptedAt(LocalDateTime attemptedAt) { this.attemptedAt = attemptedAt; }
}
