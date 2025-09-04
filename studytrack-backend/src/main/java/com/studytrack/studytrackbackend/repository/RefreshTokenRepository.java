package com.studytrack.studytrackbackend.repository;

import com.studytrack.studytrackbackend.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    
    Optional<RefreshToken> findByTokenId(String tokenId);
    
    List<RefreshToken> findByUserId(UUID userId);
    
    List<RefreshToken> findByUserIdAndIsRevokedFalse(UUID userId);
    
    @Query("SELECT rt FROM RefreshToken rt WHERE rt.tokenId = :tokenId AND rt.isRevoked = false AND rt.expiresAt > :now")
    Optional<RefreshToken> findValidTokenByTokenId(@Param("tokenId") String tokenId, @Param("now") LocalDateTime now);
    
    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.isRevoked = true WHERE rt.user.id = :userId")
    void revokeAllTokensByUserId(@Param("userId") UUID userId);
    
    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.isRevoked = true WHERE rt.tokenId = :tokenId")
    void revokeTokenByTokenId(@Param("tokenId") String tokenId);
    
    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.expiresAt < :now")
    void deleteExpiredTokens(@Param("now") LocalDateTime now);
    
    @Query("SELECT COUNT(rt) FROM RefreshToken rt WHERE rt.user.id = :userId AND rt.isRevoked = false AND rt.expiresAt > :now")
    long countValidTokensByUserId(@Param("userId") UUID userId, @Param("now") LocalDateTime now);
}
