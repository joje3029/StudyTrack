package com.studytrack.studytrackbackend.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "\"refresh_tokens\"")
public class RefreshToken {
    
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "uuid")
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(name = "token_id", nullable = false, unique = true)
    private String tokenId; // JWT의 jti 값
    
    @Column(name = "token_hash", nullable = false)
    private String tokenHash; // Refresh Token의 해시값
    
    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "last_used_at")
    private LocalDateTime lastUsedAt;
    
    @Column(name = "is_revoked")
    private Boolean isRevoked = false;
    
    @Column(name = "device_info", length = 500)
    private String deviceInfo; // User-Agent, IP 등
    
    @Column(name = "ip_address")
    private String ipAddress;
    
    // 기본 생성자
    protected RefreshToken() {}
    
    // 생성자
    public RefreshToken(User user, String tokenId, String tokenHash, LocalDateTime expiresAt) {
        this.user = user;
        this.tokenId = tokenId;
        this.tokenHash = tokenHash;
        this.expiresAt = expiresAt;
        this.isRevoked = false;
        this.createdAt = LocalDateTime.now();
    }
    
    public RefreshToken(User user, String tokenId, String tokenHash, LocalDateTime expiresAt, 
                       String deviceInfo, String ipAddress) {
        this.user = user;
        this.tokenId = tokenId;
        this.tokenHash = tokenHash;
        this.expiresAt = expiresAt;
        this.deviceInfo = deviceInfo;
        this.ipAddress = ipAddress;
        this.isRevoked = false;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getter 메서드들
    public UUID getId() {
        return id;
    }
    
    public User getUser() {
        return user;
    }
    
    public String getTokenId() {
        return tokenId;
    }
    
    public String getTokenHash() {
        return tokenHash;
    }
    
    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getLastUsedAt() {
        return lastUsedAt;
    }
    
    public Boolean getIsRevoked() {
        return isRevoked;
    }
    
    public String getDeviceInfo() {
        return deviceInfo;
    }
    
    public String getIpAddress() {
        return ipAddress;
    }
    
    // Setter 메서드들
    public void setLastUsedAt(LocalDateTime lastUsedAt) {
        this.lastUsedAt = lastUsedAt;
    }
    
    public void revoke() {
        this.isRevoked = true;
    }
    
    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }
    
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    
    // 토큰 만료 여부 확인
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiresAt);
    }
    
    // 토큰 유효성 확인
    public boolean isValid() {
        return !this.isRevoked && !this.isExpired();
    }
}
