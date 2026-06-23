package com.interviewbuddy.dto;

import com.interviewbuddy.domain.Confidence;

public class AttemptRequest {

    private Confidence confidence;
    private String personalNote;

    public Confidence getConfidence() { return confidence; }
    public void setConfidence(Confidence confidence) { this.confidence = confidence; }

    public String getPersonalNote() { return personalNote; }
    public void setPersonalNote(String personalNote) { this.personalNote = personalNote; }
}
