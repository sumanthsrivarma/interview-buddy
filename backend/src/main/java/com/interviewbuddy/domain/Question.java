package com.interviewbuddy.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "question")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;

    @Column(columnDefinition = "TEXT")
    private String answer;

    @Column(name = "follow_up_probes", columnDefinition = "TEXT")
    private String followUpProbes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Topic topic;

    @Enumerated(EnumType.STRING)
    @Column(name = "tech_stack", nullable = false, length = 50)
    private TechStack techStack;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty_level", nullable = false, length = 20)
    private DifficultyLevel difficultyLevel;

    @Column(name = "experience_range_min")
    private Integer experienceRangeMin;

    @Column(name = "experience_range_max")
    private Integer experienceRangeMax;

    @Column(name = "tags")
    private String[] tags = new String[0];

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }

    public String getFollowUpProbes() { return followUpProbes; }
    public void setFollowUpProbes(String followUpProbes) { this.followUpProbes = followUpProbes; }

    public Topic getTopic() { return topic; }
    public void setTopic(Topic topic) { this.topic = topic; }

    public TechStack getTechStack() { return techStack; }
    public void setTechStack(TechStack techStack) { this.techStack = techStack; }

    public DifficultyLevel getDifficultyLevel() { return difficultyLevel; }
    public void setDifficultyLevel(DifficultyLevel difficultyLevel) { this.difficultyLevel = difficultyLevel; }

    public Integer getExperienceRangeMin() { return experienceRangeMin; }
    public void setExperienceRangeMin(Integer experienceRangeMin) { this.experienceRangeMin = experienceRangeMin; }

    public Integer getExperienceRangeMax() { return experienceRangeMax; }
    public void setExperienceRangeMax(Integer experienceRangeMax) { this.experienceRangeMax = experienceRangeMax; }

    public String[] getTags() { return tags; }
    public void setTags(String[] tags) { this.tags = tags; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
