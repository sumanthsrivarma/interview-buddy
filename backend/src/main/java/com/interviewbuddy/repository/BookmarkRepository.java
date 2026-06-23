package com.interviewbuddy.repository;

import com.interviewbuddy.domain.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, UUID> {

    @Query("SELECT b FROM Bookmark b JOIN FETCH b.question ORDER BY b.createdAt DESC")
    List<Bookmark> findAllWithQuestion();

    Optional<Bookmark> findByQuestionId(UUID questionId);
}
