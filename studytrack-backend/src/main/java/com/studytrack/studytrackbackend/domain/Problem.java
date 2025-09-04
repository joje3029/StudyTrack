package com.studytrack.studytrackbackend.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "\"problem\"")
public class Problem {
    
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
    
    @Column(nullable = false, length = 20)
    private String type = "subjective"; // 'multiple_choice', 'subjective', 'true_false'
    
    @Column(nullable = false, columnDefinition = "text")
    private String question;
    
    @Column(nullable = false, columnDefinition = "text")
    private String answer;
    
    @Column(columnDefinition = "text")
    private String explanation;
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<String> options; // 객관식 선택지
    
    private Integer difficulty = 1; // 1(쉬움) ~ 5(어려움)
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<String> tags; // 태그 배열
    
    @Column(name = "is_public")
    private Boolean isPublic = false; // 공개 문제 여부
    
    @Column(name = "solve_count")
    private Integer solveCount = 0; // 총 풀이 횟수
    
    @Column(name = "correct_count")
    private Integer correctCount = 0; // 정답 횟수
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    // 기본 생성자
    protected Problem() {}
    
    // 생성자
    public Problem(User user, Subject subject, String type, String question, String answer) {
        this.user = user;
        this.subject = subject;
        this.type = type;
        this.question = question;
        this.answer = answer;
        this.difficulty = 1;
        this.isPublic = false;
        this.solveCount = 0;
        this.correctCount = 0;
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
    
    public String getType() {
        return type;
    }
    
    public String getQuestion() {
        return question;
    }
    
    public String getAnswer() {
        return answer;
    }
    
    public String getExplanation() {
        return explanation;
    }
    
    public List<String> getOptions() {
        return options;
    }
    
    public Integer getDifficulty() {
        return difficulty;
    }
    
    public List<String> getTags() {
        return tags;
    }
    
    public Boolean getIsPublic() {
        return isPublic;
    }
    
    public Integer getSolveCount() {
        return solveCount;
    }
    
    public Integer getCorrectCount() {
        return correctCount;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    // Setter 메서드들
    public void setType(String type) {
        this.type = type;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void setQuestion(String question) {
        this.question = question;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void setAnswer(String answer) {
        this.answer = answer;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void setExplanation(String explanation) {
        this.explanation = explanation;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void setOptions(List<String> options) {
        this.options = options;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void setTags(List<String> tags) {
        this.tags = tags;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void incrementSolveCount() {
        this.solveCount++;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void incrementCorrectCount() {
        this.correctCount++;
        this.updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
