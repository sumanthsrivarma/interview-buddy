package com.interviewbuddy.dto;

public class TopicBreakdownDto {

    private String topic;
    private long confident;
    private long needsWork;
    private long skipped;

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public long getConfident() { return confident; }
    public void setConfident(long confident) { this.confident = confident; }

    public long getNeedsWork() { return needsWork; }
    public void setNeedsWork(long needsWork) { this.needsWork = needsWork; }

    public long getSkipped() { return skipped; }
    public void setSkipped(long skipped) { this.skipped = skipped; }
}
