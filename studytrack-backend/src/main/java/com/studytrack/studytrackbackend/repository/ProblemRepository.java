package com.studytrack.studytrackbackend.repository;

import com.studytrack.studytrackbackend.domain.Problem;
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
public interface ProblemRepository extends JpaRepository<Problem, UUID> {
    
    List<Problem> findByUserIdOrderByCreatedAtDesc(UUID userId);
    
    List<Problem> findBySubjectIdOrderByCreatedAtDesc(UUID subjectId);
    
    Page<Problem> findByUserIdOrderByCreatedAtDesc(UUID userId, Pageable pageable);
    
    Page<Problem> findBySubjectIdOrderByCreatedAtDesc(UUID subjectId, Pageable pageable);
    
    Optional<Problem> findByIdAndUserId(UUID id, UUID userId);
    
    List<Problem> findByUserIdAndType(UUID userId, String type);
    
    List<Problem> findByUserIdAndDifficulty(UUID userId, Integer difficulty);
    
    List<Problem> findByIsPublicTrue();
    
    @Query("SELECT p FROM Problem p WHERE p.user.id = :userId AND p.question LIKE %:keyword%")
    List<Problem> searchByUserIdAndKeyword(@Param("userId") UUID userId, @Param("keyword") String keyword);
    
    @Query("SELECT COUNT(p) FROM Problem p WHERE p.user.id = :userId")
    long countByUserId(@Param("userId") UUID userId);
    
    @Query("SELECT COUNT(p) FROM Problem p WHERE p.subject.id = :subjectId")
    long countBySubjectId(@Param("subjectId") UUID subjectId);
    
    @Query("SELECT p FROM Problem p WHERE p.user.id = :userId AND p.type = :type ORDER BY p.createdAt DESC")
    List<Problem> findByUserIdAndTypeOrderByCreatedAtDesc(@Param("userId") UUID userId, @Param("type") String type);
    
    @Query("SELECT p FROM Problem p WHERE p.user.id = :userId ORDER BY p.solveCount DESC")
    List<Problem> findPopularProblemsByUserId(@Param("userId") UUID userId, Pageable pageable);
}
