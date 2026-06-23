package com.interviewbuddy.dto;

import com.interviewbuddy.domain.DifficultyLevel;
import com.interviewbuddy.domain.Question;
import com.interviewbuddy.domain.TechStack;
import com.interviewbuddy.domain.Topic;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class QuestionResponse {

    private UUID id;
    private String title;
    private String body;
    private String answer;
    private String followUpProbes;
    private Topic topic;
    private TechStack techStack;
    private DifficultyLevel difficultyLevel;
    private Integer experienceRangeMin;
    private Integer experienceRangeMax;
    private List<String> tags;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static QuestionResponse from(Question q) {
        QuestionResponse dto = new QuestionResponse();
        dto.setId(q.getId());
        dto.setTitle(q.getTitle());
        dto.setBody(q.getBody());
        dto.setAnswer(q.getAnswer());
        dto.setFollowUpProbes(q.getFollowUpProbes());
        dto.setTopic(q.getTopic());
        dto.setTechStack(q.getTechStack());
        dto.setDifficultyLevel(q.getDifficultyLevel());
        dto.setExperienceRangeMin(q.getExperienceRangeMin());
        dto.setExperienceRangeMax(q.getExperienceRangeMax());
        dto.setTags(q.getTags() != null ? Arrays.asList(q.getTags()) : Collections.emptyList());
        dto.setActive(q.isActive());
        dto.setCreatedAt(q.getCreatedAt());
        dto.setUpdatedAt(q.getUpdatedAt());
        return dto;
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

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
