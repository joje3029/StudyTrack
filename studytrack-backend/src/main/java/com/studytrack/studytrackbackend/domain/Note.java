package com.studytrack.studytrackbackend.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "\"note\"")
public class Note {
    
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "uuid")
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;
    
    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false, columnDefinition = "text")
    private String content;
    
    @Column(name = "content_type", length = 20)
    private String contentType = "html"; // 'html', 'markdown'
    
    @Column(name = "is_favorite")
    private Boolean isFavorite = false;
    
    @Column(name = "view_count")
    private Integer viewCount = 0;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    // 기본 생성자
    protected Note() {}
    
    // 생성자
    public Note(User user, Subject subject, String title, String content) {
        this.user = user;
        this.subject = subject;
        this.title = title;
        this.content = content;
        this.contentType = "html";
        this.isFavorite = false;
        this.viewCount = 0;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getter 메서드들
    public UUID getId() {
        return id;
    }
    
    public User getUser() {
        return user;
    }
    
    public Subject getSubject() {
        return subject;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getContent() {
        return content;
    }
    
    public String getContentType() {
        return contentType;
    }
    
    public Boolean getIsFavorite() {
        return isFavorite;
    }
    
    public Integer getViewCount() {
        return viewCount;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    // Setter 메서드들
    public void setTitle(String title) {
        this.title = title;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void setContent(String content) {
        this.content = content;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void setContentType(String contentType) {
        this.contentType = contentType;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void setIsFavorite(Boolean isFavorite) {
        this.isFavorite = isFavorite;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void incrementViewCount() {
        this.viewCount++;
        this.updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
