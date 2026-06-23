package com.interviewbuddy.repository;

import com.interviewbuddy.domain.SessionAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SessionAttemptRepository extends JpaRepository<SessionAttempt, UUID> {

    @Query("SELECT sa FROM SessionAttempt sa JOIN FETCH sa.question WHERE sa.session.id = :sessionId")
    List<SessionAttempt> findBySessionIdWithQuestion(@Param("sessionId") UUID sessionId);

    Optional<SessionAttempt> findByIdAndSessionId(UUID id, UUID sessionId);

    @Query("SELECT q.topic, sa.confidence, COUNT(sa) " +
           "FROM SessionAttempt sa JOIN sa.question q " +
           "WHERE sa.confidence IS NOT NULL " +
           "GROUP BY q.topic, sa.confidence")
    List<Object[]> findTopicConfidenceBreakdown();

    @Query("SELECT q.topic, COUNT(sa) " +
           "FROM SessionAttempt sa JOIN sa.question q " +
           "WHERE sa.confidence = 'NEEDS_WORK' " +
           "GROUP BY q.topic ORDER BY COUNT(sa) DESC")
    List<Object[]> findWeakAreas();
}
