package com.studytrack.studytrackbackend.controller;

import com.studytrack.studytrackbackend.dto.ApiResponse;
import com.studytrack.studytrackbackend.dto.subject.SubjectRequest;
import com.studytrack.studytrackbackend.dto.subject.SubjectResponse;
import com.studytrack.studytrackbackend.domain.Subject;
import com.studytrack.studytrackbackend.service.SubjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Tag(name = "학습 분야", description = "학습 분야 관리 API")
@RestController
@RequestMapping("/api/subjects")
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = "*")
public class SubjectController {
    
    private final SubjectService subjectService;
    
    @Autowired
    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }
    
    @Operation(summary = "내 학습 분야 목록 조회", description = "로그인한 사용자의 학습 분야 목록을 조회합니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", 
            description = "조회 성공",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401", 
            description = "인증 필요",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
        )
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<SubjectResponse>>> getMySubjects(Authentication authentication) {
        try {
            UUID userId = getUserIdFromAuth(authentication);
            List<Subject> subjects = subjectService.findByUserId(userId);
            
            List<SubjectResponse> responses = subjects.stream()
                .map(SubjectResponse::from)
                .collect(Collectors.toList());
            
            ApiResponse<List<SubjectResponse>> response = ApiResponse.success(responses, "조회 성공");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            ApiResponse<List<SubjectResponse>> errorResponse = ApiResponse.internalServerError();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    @Operation(summary = "학습 분야 생성", description = "새로운 학습 분야를 생성합니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201", 
            description = "생성 성공",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400", 
            description = "잘못된 요청 (중복된 분야명 등)",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401", 
            description = "인증 필요",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
        )
    })
    @PostMapping
    public ResponseEntity<ApiResponse<SubjectResponse>> createSubject(
            @Parameter(description = "학습 분야 정보", required = true)
            @Valid @RequestBody SubjectRequest request,
            Authentication authentication) {
        
        try {
            UUID userId = getUserIdFromAuth(authentication);
            
            Subject subject = subjectService.createSubject(
                userId, 
                request.getName(), 
                request.getDescription(), 
                request.getColor()
            );
            
            SubjectResponse response = SubjectResponse.from(subject);
            ApiResponse<SubjectResponse> apiResponse = ApiResponse.success(response, "분야가 생성되었습니다.");
            
            return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
            
        } catch (IllegalArgumentException e) {
            ApiResponse<SubjectResponse> errorResponse = ApiResponse.conflict(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            ApiResponse<SubjectResponse> errorResponse = ApiResponse.internalServerError();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    @Operation(summary = "학습 분야 수정", description = "기존 학습 분야 정보를 수정합니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", 
            description = "수정 성공",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400", 
            description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404", 
            description = "분야를 찾을 수 없음",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SubjectResponse>> updateSubject(
            @Parameter(description = "학습 분야 ID", required = true)
            @PathVariable String id,
            @Parameter(description = "수정할 학습 분야 정보", required = true)
            @Valid @RequestBody SubjectRequest request,
            Authentication authentication) {
        
        try {
            UUID userId = getUserIdFromAuth(authentication);
            UUID subjectId = UUID.fromString(id);
            
            Subject subject = subjectService.updateSubject(
                subjectId, 
                userId, 
                request.getName(), 
                request.getDescription(), 
                request.getColor()
            );
            
            SubjectResponse response = SubjectResponse.from(subject);
            ApiResponse<SubjectResponse> apiResponse = ApiResponse.success(response, "분야가 수정되었습니다.");
            
            return ResponseEntity.ok(apiResponse);
            
        } catch (IllegalArgumentException e) {
            ApiResponse<SubjectResponse> errorResponse = ApiResponse.notFound(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            ApiResponse<SubjectResponse> errorResponse = ApiResponse.internalServerError();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    @Operation(summary = "학습 분야 삭제", description = "학습 분야를 삭제합니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", 
            description = "삭제 성공",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404", 
            description = "분야를 찾을 수 없음",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSubject(
            @Parameter(description = "학습 분야 ID", required = true)
            @PathVariable String id,
            Authentication authentication) {
        
        try {
            UUID userId = getUserIdFromAuth(authentication);
            UUID subjectId = UUID.fromString(id);
            
            subjectService.deleteSubject(subjectId, userId);
            
            ApiResponse<Void> response = ApiResponse.success("분야가 삭제되었습니다.");
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            ApiResponse<Void> errorResponse = ApiResponse.notFound(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            ApiResponse<Void> errorResponse = ApiResponse.internalServerError();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    @Operation(summary = "학습 분야 검색", description = "키워드로 학습 분야를 검색합니다.")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<SubjectResponse>>> searchSubjects(
            @Parameter(description = "검색 키워드", required = true)
            @RequestParam String keyword,
            Authentication authentication) {
        
        try {
            UUID userId = getUserIdFromAuth(authentication);
            List<Subject> subjects = subjectService.searchSubjects(userId, keyword);
            
            List<SubjectResponse> responses = subjects.stream()
                .map(SubjectResponse::from)
                .collect(Collectors.toList());
            
            ApiResponse<List<SubjectResponse>> response = ApiResponse.success(responses, "검색 완료");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            ApiResponse<List<SubjectResponse>> errorResponse = ApiResponse.internalServerError();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    // 인증 정보에서 사용자 ID 추출하는 헬퍼 메서드
    private UUID getUserIdFromAuth(Authentication authentication) {
        // TODO: JWT에서 실제 사용자 ID 추출 로직 구현
        // 현재는 임시로 고정값 반환
        return UUID.randomUUID();
    }
}
