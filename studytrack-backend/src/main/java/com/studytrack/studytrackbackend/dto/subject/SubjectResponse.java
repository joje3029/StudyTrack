package com.studytrack.studytrackbackend.dto.subject;

import com.studytrack.studytrackbackend.domain.Subject;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "학습 분야 응답 DTO")
public class SubjectResponse {
    
    @Schema(description = "분야 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private String id;
    
    @Schema(description = "분야명", example = "수학")
    private String name;
    
    @Schema(description = "설명", example = "고등학교 수학 과정")
    private String description;
    
    @Schema(description = "색상 (Hex)", example = "#3B82F6")
    private String color;
    
    @Schema(description = "정렬 순서", example = "1")
    private Integer sortOrder;
    
    @Schema(description = "생성일시", example = "2025-09-04T10:30:00")
    private LocalDateTime createdAt;
    
    @Schema(description = "수정일시", example = "2025-09-04T10:30:00")
    private LocalDateTime updatedAt;
    
    // 기본 생성자
    public SubjectResponse() {}
    
    // Entity로부터 생성하는 정적 팩토리 메서드
    public static SubjectResponse from(Subject subject) {
        SubjectResponse response = new SubjectResponse();
        response.id = subject.getId().toString();
        response.name = subject.getName();
        response.description = subject.getDescription();
        response.color = subject.getColor();
        response.sortOrder = subject.getSortOrder();
        response.createdAt = subject.getCreatedAt();
        response.updatedAt = subject.getUpdatedAt();
        return response;
    }
    
    // Getter와 Setter
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getColor() {
        return color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
    
    public Integer getSortOrder() {
        return sortOrder;
    }
    
    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
