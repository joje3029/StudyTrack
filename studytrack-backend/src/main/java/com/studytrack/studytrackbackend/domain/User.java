package com.studytrack.studytrackbackend.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "\"user\"")
public class User {
    
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "uuid")
    private UUID id;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(length = 255)
    private String password; // 자체 로그인용 (소셜 로그인 시 NULL)
    
    @Column(nullable = false, length = 100)
    private String nickname;
    
    @Column(nullable = false, length = 20)
    private String provider = "self"; // 'self', 'google', 'naver', 'kakao'
    
    @Column(name = "social_id", length = 255)
    private String socialId; // 소셜 로그인 고유 ID
    
    @Column(nullable = false, length = 20)
    private String status = "active"; // 'active', 'inactive', 'suspended'
    
    @Column(name = "email_verified")
    private Boolean emailVerified = false;
    
    @Column(name = "profile_image_url", length = 500)
    private String profileImageUrl;
    
    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;
    
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    // 기본 생성자
    protected User() {}
    
    // 자체 로그인용 생성자
    public User(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.provider = "self";
        this.status = "active";
        this.emailVerified = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // 소셜 로그인용 생성자
    public User(String email, String nickname, String provider, String socialId) {
        this.email = email;
        this.nickname = nickname;
        this.provider = provider;
        this.socialId = socialId;
        this.status = "active";
        this.emailVerified = true; // 소셜 로그인은 이메일 인증된 것으로 간주
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getter 메서드들
    public UUID getId() {
        return id;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public String getNickname() {
        return nickname;
    }
    
    public String getProvider() {
        return provider;
    }
    
    public String getSocialId() {
        return socialId;
    }
    
    public String getStatus() {
        return status;
    }
    
    public Boolean getEmailVerified() {
        return emailVerified;
    }
    
    public String getProfileImageUrl() {
        return profileImageUrl;
    }
    
    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }
    
    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    // Setter 메서드들
    public void setPassword(String password) {
        this.password = password;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void setNickname(String nickname) {
        this.nickname = nickname;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void setStatus(String status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void setLastLoginAt(LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
        this.updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
