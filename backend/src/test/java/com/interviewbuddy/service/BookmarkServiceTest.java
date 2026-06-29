package com.interviewbuddy.service;

import com.interviewbuddy.domain.Bookmark;
import com.interviewbuddy.domain.DifficultyLevel;
import com.interviewbuddy.domain.Question;
import com.interviewbuddy.domain.Topic;
import com.interviewbuddy.dto.BookmarkRequest;
import com.interviewbuddy.dto.BookmarkResponse;
import com.interviewbuddy.dto.UpdateBookmarkNoteRequest;
import com.interviewbuddy.repository.BookmarkRepository;
import com.interviewbuddy.repository.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookmarkServiceTest {

    @Mock
    private BookmarkRepository bookmarkRepository;

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private BookmarkService bookmarkService;

    private Question question;
    private Bookmark bookmark;

    @BeforeEach
    void setUp() {
        question = new Question();
        question.setId(UUID.randomUUID());
        question.setTitle("What is the JVM?");
        question.setTopic(Topic.JVM_INTERNALS);
        question.setDifficultyLevel(DifficultyLevel.MEDIUM);

        bookmark = new Bookmark();
        bookmark.setId(UUID.randomUUID());
        bookmark.setQuestion(question);
        bookmark.setNote("My note");
        bookmark.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void listAll_returnsBookmarksOrderedByCreatedAtDesc() {
        when(bookmarkRepository.findAllByOrderByCreatedAtDesc()).thenReturn(List.of(bookmark));

        List<BookmarkResponse> result = bookmarkService.listAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getQuestionText()).isEqualTo("What is the JVM?");
        assertThat(result.get(0).getNote()).isEqualTo("My note");
    }

    @Test
    void updateNote_happyPath_updatesNoteAndReturnsResponse() {
        UpdateBookmarkNoteRequest request = new UpdateBookmarkNoteRequest();
        request.setNote("Updated note");
        when(bookmarkRepository.findById(bookmark.getId())).thenReturn(Optional.of(bookmark));
        when(bookmarkRepository.save(bookmark)).thenReturn(bookmark);

        BookmarkResponse result = bookmarkService.updateNote(bookmark.getId(), request);

        assertThat(result.getNote()).isEqualTo("Updated note");
        verify(bookmarkRepository).save(bookmark);
    }

    @Test
    void updateNote_notFound_throws404() {
        UUID unknownId = UUID.randomUUID();
        when(bookmarkRepository.findById(unknownId)).thenReturn(Optional.empty());

        UpdateBookmarkNoteRequest request = new UpdateBookmarkNoteRequest();
        request.setNote("note");

        assertThatThrownBy(() -> bookmarkService.updateNote(unknownId, request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("404");
    }

    @Test
    void delete_happyPath_deletesBookmark() {
        when(bookmarkRepository.existsById(bookmark.getId())).thenReturn(true);

        bookmarkService.delete(bookmark.getId());

        verify(bookmarkRepository).deleteById(bookmark.getId());
    }

    @Test
    void delete_notFound_throws404() {
        UUID unknownId = UUID.randomUUID();
        when(bookmarkRepository.existsById(unknownId)).thenReturn(false);

        assertThatThrownBy(() -> bookmarkService.delete(unknownId))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("404");
    }
}
