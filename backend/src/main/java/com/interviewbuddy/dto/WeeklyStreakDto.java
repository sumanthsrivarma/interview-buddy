package com.interviewbuddy.dto;

public class WeeklyStreakDto {

    private String week;
    private long sessionCount;

    public String getWeek() { return week; }
    public void setWeek(String week) { this.week = week; }

    public long getSessionCount() { return sessionCount; }
    public void setSessionCount(long sessionCount) { this.sessionCount = sessionCount; }
}
