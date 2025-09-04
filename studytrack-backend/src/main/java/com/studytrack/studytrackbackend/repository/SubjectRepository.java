package com.studytrack.studytrackbackend.repository;

import com.studytrack.studytrackbackend.domain.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, UUID> {
    
    List<Subject> findByUserIdOrderBySortOrderAsc(UUID userId);
    
    Optional<Subject> findByUserIdAndName(UUID userId, String name);
    
    boolean existsByUserIdAndName(UUID userId, String name);
    
    @Query("SELECT s FROM Subject s WHERE s.user.id = :userId AND s.name LIKE %:keyword%")
    List<Subject> findByUserIdAndNameContaining(@Param("userId") UUID userId, @Param("keyword") String keyword);
    
    @Query("SELECT COUNT(s) FROM Subject s WHERE s.user.id = :userId")
    long countByUserId(@Param("userId") UUID userId);
    
    @Query("SELECT s FROM Subject s WHERE s.user.id = :userId ORDER BY s.updatedAt DESC")
    List<Subject> findByUserIdOrderByUpdatedAtDesc(@Param("userId") UUID userId);
}
