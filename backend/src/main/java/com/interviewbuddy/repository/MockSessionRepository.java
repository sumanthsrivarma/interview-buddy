package com.interviewbuddy.repository;

import com.interviewbuddy.domain.MockSession;
import com.interviewbuddy.domain.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MockSessionRepository extends JpaRepository<MockSession, UUID> {

    List<MockSession> findByStatusOrderByStartedAtDesc(SessionStatus status);

    List<MockSession> findAllByOrderByStartedAtDesc();

    @Query(value = "SELECT DATE_TRUNC('week', completed_at) as week, COUNT(*) as count " +
                   "FROM mock_session " +
                   "WHERE status = 'COMPLETED' AND completed_at IS NOT NULL " +
                   "GROUP BY DATE_TRUNC('week', completed_at) " +
                   "ORDER BY week DESC " +
                   "LIMIT 12",
           nativeQuery = true)
    List<Object[]> findWeeklyStreak();
}
