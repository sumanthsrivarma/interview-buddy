package com.interviewbuddy.dto;

import com.interviewbuddy.domain.DifficultyLevel;
import com.interviewbuddy.domain.TechStack;
import com.interviewbuddy.domain.Topic;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class QuestionRequest {

    @NotBlank
    @Size(max = 500)
    private String title;

    @NotBlank
    private String body;

    private String answer;

    private String followUpProbes;

    @NotNull
    private Topic topic;

    @NotNull
    private TechStack techStack;

    @NotNull
    private DifficultyLevel difficultyLevel;

    private Integer experienceRangeMin;

    private Integer experienceRangeMax;

    private List<String> tags;

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
}
