package com.interviewbuddy.dto;

import java.util.UUID;

public class BookmarkRequest {

    private UUID questionId;

    public UUID getQuestionId() {
        return questionId;
    }

    public void setQuestionId(UUID questionId) {
        this.questionId = questionId;
    }
}
