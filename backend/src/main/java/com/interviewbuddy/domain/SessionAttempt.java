package com.interviewbuddy.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "session_attempt")
public class SessionAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private MockSession session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Confidence confidence;

    @Column(name = "personal_note", columnDefinition = "TEXT")
    private String personalNote;

    @Column(name = "attempted_at", nullable = false, updatable = false)
    private LocalDateTime attemptedAt;

    @PrePersist
    protected void onCreate() {
        attemptedAt = LocalDateTime.now();
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public MockSession getSession() { return session; }
    public void setSession(MockSession session) { this.session = session; }

    public Question getQuestion() { return question; }
    public void setQuestion(Question question) { this.question = question; }

    public Confidence getConfidence() { return confidence; }
    public void setConfidence(Confidence confidence) { this.confidence = confidence; }

    public String getPersonalNote() { return personalNote; }
    public void setPersonalNote(String personalNote) { this.personalNote = personalNote; }

    public LocalDateTime getAttemptedAt() { return attemptedAt; }
    public void setAttemptedAt(LocalDateTime attemptedAt) { this.attemptedAt = attemptedAt; }
}
