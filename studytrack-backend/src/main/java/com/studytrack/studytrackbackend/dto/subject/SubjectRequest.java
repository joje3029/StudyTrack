package com.studytrack.studytrackbackend.dto.subject;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "학습 분야 생성/수정 요청 DTO")
public class SubjectRequest {
    
    @Schema(description = "분야명", example = "수학")
    @NotBlank(message = "분야명은 필수입니다.")
    @Size(max = 100, message = "분야명은 100자 이하여야 합니다.")
    private String name;
    
    @Schema(description = "설명", example = "고등학교 수학 과정")
    @Size(max = 500, message = "설명은 500자 이하여야 합니다.")
    private String description;
    
    @Schema(description = "색상 (Hex)", example = "#3B82F6")
    private String color;
    
    // 기본 생성자
    public SubjectRequest() {}
    
    // 생성자
    public SubjectRequest(String name, String description, String color) {
        this.name = name;
        this.description = description;
        this.color = color;
    }
    
    // Getter와 Setter
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
}
