package com.interviewbuddy.service;

import com.interviewbuddy.domain.Confidence;
import com.interviewbuddy.domain.Topic;
import com.interviewbuddy.dto.TopicBreakdownDto;
import com.interviewbuddy.dto.WeakAreaDto;
import com.interviewbuddy.dto.WeeklyStreakDto;
import com.interviewbuddy.repository.MockSessionRepository;
import com.interviewbuddy.repository.SessionAttemptRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    private final SessionAttemptRepository attemptRepository;
    private final MockSessionRepository sessionRepository;

    public DashboardService(SessionAttemptRepository attemptRepository,
                            MockSessionRepository sessionRepository) {
        this.attemptRepository = attemptRepository;
        this.sessionRepository = sessionRepository;
    }

    public List<TopicBreakdownDto> getSummary() {
        List<Object[]> rows = attemptRepository.findTopicConfidenceBreakdown();
        Map<String, TopicBreakdownDto> map = new LinkedHashMap<>();
        for (Object[] row : rows) {
            String topicName = row[0].toString();
            Confidence confidence = (Confidence) row[1];
            long count = (long) row[2];
            map.computeIfAbsent(topicName, t -> {
                TopicBreakdownDto dto = new TopicBreakdownDto();
                dto.setTopic(t);
                return dto;
            });
            TopicBreakdownDto dto = map.get(topicName);
            switch (confidence) {
                case CONFIDENT  -> dto.setConfident(count);
                case NEEDS_WORK -> dto.setNeedsWork(count);
                case SKIPPED    -> dto.setSkipped(count);
            }
        }
        return new ArrayList<>(map.values());
    }

    public List<WeakAreaDto> getWeakAreas() {
        List<Object[]> rows = attemptRepository.findWeakAreas();
        List<WeakAreaDto> result = new ArrayList<>();
        for (Object[] row : rows) {
            WeakAreaDto dto = new WeakAreaDto();
            dto.setTopic(row[0].toString());
            dto.setNeedsWorkCount((long) row[1]);
            result.add(dto);
        }
        return result;
    }

    public List<WeeklyStreakDto> getStreak() {
        List<Object[]> rows = sessionRepository.findWeeklyStreak();
        List<WeeklyStreakDto> result = new ArrayList<>();
        for (Object[] row : rows) {
            WeeklyStreakDto dto = new WeeklyStreakDto();
            if (row[0] instanceof Timestamp ts) {
                dto.setWeek(ts.toLocalDateTime().toLocalDate().toString());
            } else {
                dto.setWeek(row[0].toString());
            }
            dto.setSessionCount(((Number) row[1]).longValue());
            result.add(dto);
        }
        return result;
    }
}
