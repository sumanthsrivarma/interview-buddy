package com.interviewbuddy.service;

import com.interviewbuddy.domain.*;
import com.interviewbuddy.dto.*;
import com.interviewbuddy.repository.MockSessionRepository;
import com.interviewbuddy.repository.QuestionRepository;
import com.interviewbuddy.repository.SessionAttemptRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MockSessionService {

    private final MockSessionRepository sessionRepository;
    private final SessionAttemptRepository attemptRepository;
    private final QuestionRepository questionRepository;

    public MockSessionService(MockSessionRepository sessionRepository,
                               SessionAttemptRepository attemptRepository,
                               QuestionRepository questionRepository) {
        this.sessionRepository = sessionRepository;
        this.attemptRepository = attemptRepository;
        this.questionRepository = questionRepository;
    }

    public List<SessionResponse> findAll() {
        return sessionRepository.findAllByOrderByStartedAtDesc()
                .stream().map(SessionResponse::from).collect(Collectors.toList());
    }

    public List<SessionResponse> findHistory() {
        return sessionRepository.findByStatusOrderByStartedAtDesc(SessionStatus.COMPLETED)
                .stream().map(SessionResponse::from).collect(Collectors.toList());
    }

    public SessionDetailResponse findById(UUID id) {
        MockSession session = sessionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Session not found"));
        List<SessionAttempt> attempts = attemptRepository.findBySessionIdWithQuestion(id);
        return SessionDetailResponse.from(session, attempts);
    }

    @Transactional
    public SessionDetailResponse create(SessionRequest request) {
        List<Question> matching = questionRepository.findAll(buildQuestionSpec(request));

        if (matching.size() < request.getQuestionCount()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Not enough matching questions: found " + matching.size()
                    + ", requested " + request.getQuestionCount());
        }

        Collections.shuffle(matching);
        List<Question> selected = matching.subList(0, request.getQuestionCount());

        MockSession session = new MockSession();
        session.setName(request.getName());
        session.setTopic(request.getTopic());
        session.setTechStack(request.getTechStack());
        session.setDifficultyLevel(request.getDifficultyLevel());
        session.setExperienceRange(request.getExperienceRange());
        session.setQuestionCount(request.getQuestionCount());
        MockSession saved = sessionRepository.save(session);

        List<SessionAttempt> attempts = selected.stream().map(q -> {
            SessionAttempt attempt = new SessionAttempt();
            attempt.setSession(saved);
            attempt.setQuestion(q);
            return attempt;
        }).collect(Collectors.toList());
        attemptRepository.saveAll(attempts);

        return SessionDetailResponse.from(saved, attempts);
    }

    @Transactional
    public SessionDetailResponse complete(UUID id) {
        MockSession session = sessionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Session not found"));
        session.setStatus(SessionStatus.COMPLETED);
        session.setCompletedAt(LocalDateTime.now());
        sessionRepository.save(session);
        List<SessionAttempt> attempts = attemptRepository.findBySessionIdWithQuestion(id);
        return SessionDetailResponse.from(session, attempts);
    }

    @Transactional
    public AttemptResponse updateAttempt(UUID sessionId, UUID attemptId, AttemptRequest request) {
        sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Session not found"));
        SessionAttempt attempt = attemptRepository.findByIdAndSessionId(attemptId, sessionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Attempt not found"));
        attempt.setConfidence(request.getConfidence());
        attempt.setPersonalNote(request.getPersonalNote());
        return AttemptResponse.from(attemptRepository.save(attempt));
    }

    public List<AttemptResponse> getAttempts(UUID sessionId) {
        sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Session not found"));
        return attemptRepository.findBySessionIdWithQuestion(sessionId)
                .stream().map(AttemptResponse::from).collect(Collectors.toList());
    }

    private Specification<Question> buildQuestionSpec(SessionRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.isTrue(root.get("active")));
            if (request.getTopic() != null) {
                predicates.add(cb.equal(root.get("topic"), request.getTopic()));
            }
            if (request.getTechStack() != null) {
                predicates.add(cb.equal(root.get("techStack"), request.getTechStack()));
            }
            if (request.getDifficultyLevel() != null) {
                predicates.add(cb.equal(root.get("difficultyLevel"), request.getDifficultyLevel()));
            }
            if (request.getExperienceRange() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("experienceRangeMin"), request.getExperienceRange()));
                predicates.add(cb.greaterThanOrEqualTo(root.get("experienceRangeMax"), request.getExperienceRange()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
