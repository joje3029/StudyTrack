package com.studytrack.studytrackbackend.repository;

import com.studytrack.studytrackbackend.domain.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NoteRepository extends JpaRepository<Note, UUID> {
    
    List<Note> findByUserIdOrderByUpdatedAtDesc(UUID userId);
    
    List<Note> findBySubjectIdOrderByUpdatedAtDesc(UUID subjectId);
    
    Page<Note> findByUserIdOrderByUpdatedAtDesc(UUID userId, Pageable pageable);
    
    Page<Note> findBySubjectIdOrderByUpdatedAtDesc(UUID subjectId, Pageable pageable);
    
    Optional<Note> findByIdAndUserId(UUID id, UUID userId);
    
    List<Note> findByUserIdAndIsFavoriteTrue(UUID userId);
    
    @Query("SELECT n FROM Note n WHERE n.user.id = :userId AND (n.title LIKE %:keyword% OR n.content LIKE %:keyword%)")
    List<Note> searchByUserIdAndKeyword(@Param("userId") UUID userId, @Param("keyword") String keyword);
    
    @Query("SELECT COUNT(n) FROM Note n WHERE n.user.id = :userId")
    long countByUserId(@Param("userId") UUID userId);
    
    @Query("SELECT COUNT(n) FROM Note n WHERE n.subject.id = :subjectId")
    long countBySubjectId(@Param("subjectId") UUID subjectId);
    
    @Query("SELECT n FROM Note n WHERE n.user.id = :userId ORDER BY n.viewCount DESC")
    List<Note> findPopularNotesByUserId(@Param("userId") UUID userId, Pageable pageable);
}
