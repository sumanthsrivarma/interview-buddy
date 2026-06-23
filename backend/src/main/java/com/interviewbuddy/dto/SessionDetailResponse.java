package com.interviewbuddy.dto;

import com.interviewbuddy.domain.Confidence;
import com.interviewbuddy.domain.MockSession;
import com.interviewbuddy.domain.SessionAttempt;

import java.util.List;
import java.util.stream.Collectors;

public class SessionDetailResponse extends SessionResponse {

    private List<AttemptResponse> attempts;
    private long confidentCount;
    private long needsWorkCount;
    private long skippedCount;
    private long notRatedCount;

    public static SessionDetailResponse from(MockSession session, List<SessionAttempt> attempts) {
        SessionDetailResponse dto = new SessionDetailResponse();
        dto.setId(session.getId());
        dto.setName(session.getName());
        dto.setTopic(session.getTopic());
        dto.setTechStack(session.getTechStack());
        dto.setDifficultyLevel(session.getDifficultyLevel());
        dto.setExperienceRange(session.getExperienceRange());
        dto.setQuestionCount(session.getQuestionCount());
        dto.setStatus(session.getStatus());
        dto.setStartedAt(session.getStartedAt());
        dto.setCompletedAt(session.getCompletedAt());
        dto.setAttempts(attempts.stream().map(AttemptResponse::from).collect(Collectors.toList()));
        dto.setConfidentCount(attempts.stream().filter(a -> a.getConfidence() == Confidence.CONFIDENT).count());
        dto.setNeedsWorkCount(attempts.stream().filter(a -> a.getConfidence() == Confidence.NEEDS_WORK).count());
        dto.setSkippedCount(attempts.stream().filter(a -> a.getConfidence() == Confidence.SKIPPED).count());
        dto.setNotRatedCount(attempts.stream().filter(a -> a.getConfidence() == null).count());
        return dto;
    }

    public List<AttemptResponse> getAttempts() { return attempts; }
    public void setAttempts(List<AttemptResponse> attempts) { this.attempts = attempts; }

    public long getConfidentCount() { return confidentCount; }
    public void setConfidentCount(long confidentCount) { this.confidentCount = confidentCount; }

    public long getNeedsWorkCount() { return needsWorkCount; }
    public void setNeedsWorkCount(long needsWorkCount) { this.needsWorkCount = needsWorkCount; }

    public long getSkippedCount() { return skippedCount; }
    public void setSkippedCount(long skippedCount) { this.skippedCount = skippedCount; }

    public long getNotRatedCount() { return notRatedCount; }
    public void setNotRatedCount(long notRatedCount) { this.notRatedCount = notRatedCount; }
}
