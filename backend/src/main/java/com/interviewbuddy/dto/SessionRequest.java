package com.interviewbuddy.dto;

import com.interviewbuddy.domain.DifficultyLevel;
import com.interviewbuddy.domain.TechStack;
import com.interviewbuddy.domain.Topic;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SessionRequest {

    @NotBlank
    private String name;

    private Topic topic;
    private TechStack techStack;
    private DifficultyLevel difficultyLevel;
    private Integer experienceRange;

    @NotNull
    @Min(1)
    @Max(50)
    private Integer questionCount;

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
}
