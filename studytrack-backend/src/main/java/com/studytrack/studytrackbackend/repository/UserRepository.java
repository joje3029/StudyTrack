package com.studytrack.studytrackbackend.repository;

import com.studytrack.studytrackbackend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByProviderAndSocialId(String provider, String socialId);
    
    boolean existsByEmail(String email);
    
    boolean existsByNickname(String nickname);
    
    @Query("SELECT u FROM User u WHERE u.status = :status AND u.deletedAt IS NULL")
    Optional<User> findByIdAndStatus(@Param("status") String status);
    
    @Query("SELECT u FROM User u WHERE u.email = :email AND u.status = 'active' AND u.deletedAt IS NULL")
    Optional<User> findActiveUserByEmail(@Param("email") String email);
    
    @Query("SELECT u FROM User u WHERE u.provider = :provider AND u.socialId = :socialId AND u.status = 'active' AND u.deletedAt IS NULL")
    Optional<User> findActiveUserByProviderAndSocialId(@Param("provider") String provider, @Param("socialId") String socialId);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.createdAt >= :startDate AND u.deletedAt IS NULL")
    long countNewUsersAfter(@Param("startDate") LocalDateTime startDate);
}
