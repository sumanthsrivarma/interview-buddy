package com.interviewbuddy.service;

import com.interviewbuddy.domain.DifficultyLevel;
import com.interviewbuddy.domain.Question;
import com.interviewbuddy.domain.TechStack;
import com.interviewbuddy.domain.Topic;
import com.interviewbuddy.dto.QuestionRequest;
import com.interviewbuddy.dto.QuestionResponse;
import com.interviewbuddy.repository.QuestionRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class QuestionService {

    private final QuestionRepository repository;

    public QuestionService(QuestionRepository repository) {
        this.repository = repository;
    }

    public Page<QuestionResponse> findAll(
            Topic topic,
            TechStack techStack,
            DifficultyLevel difficultyLevel,
            boolean includeInactive,
            Pageable pageable) {

        Specification<Question> spec = buildSpec(topic, techStack, difficultyLevel, includeInactive);
        return repository.findAll(spec, pageable).map(QuestionResponse::from);
    }

    public QuestionResponse findById(UUID id) {
        return repository.findById(id)
                .map(QuestionResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found"));
    }

    @Transactional
    public QuestionResponse create(QuestionRequest request) {
        Question question = new Question();
        applyRequest(question, request);
        question.setActive(true);
        return QuestionResponse.from(repository.save(question));
    }

    @Transactional
    public QuestionResponse update(UUID id, QuestionRequest request) {
        Question question = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found"));
        applyRequest(question, request);
        return QuestionResponse.from(repository.save(question));
    }

    @Transactional
    public void deactivate(UUID id) {
        Question question = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found"));
        question.setActive(false);
        repository.save(question);
    }

    private void applyRequest(Question question, QuestionRequest request) {
        question.setTitle(request.getTitle());
        question.setBody(request.getBody());
        question.setAnswer(request.getAnswer());
        question.setFollowUpProbes(request.getFollowUpProbes());
        question.setTopic(request.getTopic());
        question.setTechStack(request.getTechStack());
        question.setDifficultyLevel(request.getDifficultyLevel());
        question.setExperienceRangeMin(request.getExperienceRangeMin());
        question.setExperienceRangeMax(request.getExperienceRangeMax());
        question.setTags(request.getTags() != null
                ? request.getTags().toArray(String[]::new)
                : new String[0]);
    }

    private Specification<Question> buildSpec(
            Topic topic,
            TechStack techStack,
            DifficultyLevel difficultyLevel,
            boolean includeInactive) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!includeInactive) {
                predicates.add(cb.isTrue(root.get("active")));
            }
            if (topic != null) {
                predicates.add(cb.equal(root.get("topic"), topic));
            }
            if (techStack != null) {
                predicates.add(cb.equal(root.get("techStack"), techStack));
            }
            if (difficultyLevel != null) {
                predicates.add(cb.equal(root.get("difficultyLevel"), difficultyLevel));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
