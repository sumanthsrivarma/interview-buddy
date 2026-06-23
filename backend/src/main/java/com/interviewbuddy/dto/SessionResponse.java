package com.interviewbuddy.dto;

import com.interviewbuddy.domain.DifficultyLevel;
import com.interviewbuddy.domain.MockSession;
import com.interviewbuddy.domain.SessionStatus;
import com.interviewbuddy.domain.TechStack;
import com.interviewbuddy.domain.Topic;

import java.time.LocalDateTime;
import java.util.UUID;

public class SessionResponse {

    private UUID id;
    private String name;
    private Topic topic;
    private TechStack techStack;
    private DifficultyLevel difficultyLevel;
    private Integer experienceRange;
    private Integer questionCount;
    private SessionStatus status;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;

    public static SessionResponse from(MockSession s) {
        SessionResponse dto = new SessionResponse();
        dto.setId(s.getId());
        dto.setName(s.getName());
        dto.setTopic(s.getTopic());
        dto.setTechStack(s.getTechStack());
        dto.setDifficultyLevel(s.getDifficultyLevel());
        dto.setExperienceRange(s.getExperienceRange());
        dto.setQuestionCount(s.getQuestionCount());
        dto.setStatus(s.getStatus());
        dto.setStartedAt(s.getStartedAt());
        dto.setCompletedAt(s.getCompletedAt());
        return dto;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Topic getTopic() { return topic; }
    public void setTopic(Topic topic) { this.topic = topic; }

    public TechStack getTechStack() { return techStack; }
    public void setTechStack(TechStack techStack) { this.techStack = techStack; }

    public DifficultyLevel getDifficultyLevel() { return difficultyLevel; }
    public void setDifficultyLevel(DifficultyLevel difficultyLevel) { this.difficultyLevel = difficultyLevel; }

    public Integer getExperienceRange() { return experienceRange; }
    public void setExperienceRange(Integer experienceRange) { this.experienceRange = experienceRange; }

    public Integer getQuestionCount() { return questionCount; }
    public void setQuestionCount(Integer questionCount) { this.questionCount = questionCount; }

    public SessionStatus getStatus() { return status; }
    public void setStatus(SessionStatus status) { this.status = status; }

    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
}
