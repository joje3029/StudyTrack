package com.studytrack.studytrackbackend.controller;

import com.studytrack.studytrackbackend.dto.ApiResponse;
import com.studytrack.studytrackbackend.dto.auth.AuthResponse;
import com.studytrack.studytrackbackend.dto.auth.LoginRequest;
import com.studytrack.studytrackbackend.dto.auth.RegisterRequest;
import com.studytrack.studytrackbackend.domain.User;
import com.studytrack.studytrackbackend.service.UserService;
import com.studytrack.studytrackbackend.jwt.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Tag(name = "인증", description = "회원가입, 로그인, 로그아웃 관련 API")
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    
    @Autowired
    public AuthController(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }
    
    @Operation(summary = "회원가입", description = "새로운 사용자 계정을 생성합니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201", 
            description = "회원가입 성공",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400", 
            description = "잘못된 요청 (이메일/닉네임 중복, 유효성 검사 실패)",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
        )
    })
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Parameter(description = "회원가입 정보", required = true)
            @Valid @RequestBody RegisterRequest request) {
        
        try {
            // 사용자 생성
            User user = userService.registerUser(
                request.getEmail(), 
                request.getPassword(), 
                request.getNickname()
            );
            
            // JWT 토큰 생성
            String accessToken = jwtTokenProvider.createAccessToken(user.getEmail(), "USER");
            String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail(), "USER");
            
            // 응답 생성
            AuthResponse authResponse = new AuthResponse(
                accessToken,
                refreshToken,
                user.getId().toString(),
                user.getEmail(),
                user.getNickname()
            );
            
            ApiResponse<AuthResponse> response = ApiResponse.success(authResponse, "회원가입이 완료되었습니다.");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (IllegalArgumentException e) {
            ApiResponse<AuthResponse> errorResponse = ApiResponse.conflict(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            ApiResponse<AuthResponse> errorResponse = ApiResponse.internalServerError();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인합니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", 
            description = "로그인 성공",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401", 
            description = "인증 실패 (잘못된 이메일/비밀번호)",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
        )
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Parameter(description = "로그인 정보", required = true)
            @Valid @RequestBody LoginRequest request) {
        
        try {
            // 사용자 조회
            Optional<User> userOpt = userService.findByEmail(request.getEmail());
            if (userOpt.isEmpty()) {
                ApiResponse<AuthResponse> errorResponse = ApiResponse.error("41004", "유효하지 않은 인증 정보입니다.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }
            
            User user = userOpt.get();
            
            // 비밀번호 검증
            if (!userService.validatePassword(user, request.getPassword())) {
                ApiResponse<AuthResponse> errorResponse = ApiResponse.error("41004", "유효하지 않은 인증 정보입니다.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }
            
            // 로그인 시간 업데이트
            userService.updateLastLoginTime(user.getId());
            
            // JWT 토큰 생성
            String accessToken = jwtTokenProvider.createAccessToken(user.getEmail(), "USER");
            String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail(), "USER");
            
            // 응답 생성
            AuthResponse authResponse = new AuthResponse(
                accessToken,
                refreshToken,
                user.getId().toString(),
                user.getEmail(),
                user.getNickname()
            );
            
            ApiResponse<AuthResponse> response = ApiResponse.success(authResponse, "로그인 성공");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            ApiResponse<AuthResponse> errorResponse = ApiResponse.internalServerError();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    @Operation(summary = "로그아웃", description = "현재 세션을 종료합니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", 
            description = "로그아웃 성공",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
        )
    })
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        // TODO: Refresh Token 무효화 로직 추가
        ApiResponse<Void> response = ApiResponse.success("로그아웃이 완료되었습니다.");
        return ResponseEntity.ok(response);
    }
}
