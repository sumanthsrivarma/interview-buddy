package com.interviewbuddy.service;

import com.interviewbuddy.domain.Bookmark;
import com.interviewbuddy.domain.Question;
import com.interviewbuddy.dto.BookmarkRequest;
import com.interviewbuddy.dto.BookmarkResponse;
import com.interviewbuddy.dto.UpdateBookmarkNoteRequest;
import com.interviewbuddy.repository.BookmarkRepository;
import com.interviewbuddy.repository.QuestionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final QuestionRepository questionRepository;

    public BookmarkService(BookmarkRepository bookmarkRepository, QuestionRepository questionRepository) {
        this.bookmarkRepository = bookmarkRepository;
        this.questionRepository = questionRepository;
    }

    public List<BookmarkResponse> listAll() {
        return bookmarkRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(BookmarkResponse::from)
                .toList();
    }

    @Transactional
    public BookmarkResponse create(BookmarkRequest request) {
        Question question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found"));
        Bookmark bookmark = new Bookmark();
        bookmark.setQuestion(question);
        return BookmarkResponse.from(bookmarkRepository.save(bookmark));
    }

    @Transactional
    public BookmarkResponse updateNote(UUID id, UpdateBookmarkNoteRequest request) {
        Bookmark bookmark = bookmarkRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bookmark not found"));
        bookmark.setNote(request.getNote());
        return BookmarkResponse.from(bookmarkRepository.save(bookmark));
    }

    @Transactional
    public void delete(UUID id) {
        if (!bookmarkRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Bookmark not found");
        }
        bookmarkRepository.deleteById(id);
    }
}
