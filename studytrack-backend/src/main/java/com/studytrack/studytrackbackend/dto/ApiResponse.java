package com.studytrack.studytrackbackend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 공통 API 응답 DTO
 * 모든 REST API 응답에 사용되는 표준 형식
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    
    private String guid;
    private String resultCode;
    private String resultMessage;
    private T data;
    
    // 기본 생성자
    public ApiResponse() {}
    
    // 성공 응답 생성자
    public ApiResponse(T data) {
        this.guid = generateGuid();
        this.resultCode = "00000";
        this.resultMessage = "작업이 성공적으로 완료되었습니다.";
        this.data = data;
    }
    
    // 커스텀 메시지 성공 응답 생성자
    public ApiResponse(T data, String message) {
        this.guid = generateGuid();
        this.resultCode = "00000";
        this.resultMessage = message;
        this.data = data;
    }
    
    // 오류 응답 생성자
    public ApiResponse(String resultCode, String resultMessage) {
        this.guid = generateGuid();
        this.resultCode = resultCode;
        this.resultMessage = resultMessage;
        this.data = null;
    }
    
    // 전체 필드 생성자
    public ApiResponse(String guid, String resultCode, String resultMessage, T data) {
        this.guid = guid;
        this.resultCode = resultCode;
        this.resultMessage = resultMessage;
        this.data = data;
    }
    
    // GUID 생성 메서드
    private String generateGuid() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = String.format("%02d", (int) (Math.random() * 100));
        return "G" + timestamp + random;
    }
    
    // 정적 팩토리 메서드들
    
    /**
     * 성공 응답 생성
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(data);
    }
    
    /**
     * 성공 응답 생성 (커스텀 메시지)
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(data, message);
    }
    
    /**
     * 데이터 없는 성공 응답 생성
     */
    public static ApiResponse<Void> success() {
        ApiResponse<Void> response = new ApiResponse<>();
        response.guid = response.generateGuid();
        response.resultCode = "00000";
        response.resultMessage = "작업이 성공적으로 완료되었습니다.";
        response.data = null;
        return response;
    }
    
    /**
     * 데이터 없는 성공 응답 생성 (커스텀 메시지)
     */
    public static ApiResponse<Void> success(String message) {
        ApiResponse<Void> response = new ApiResponse<>();
        response.guid = response.generateGuid();
        response.resultCode = "00000";
        response.resultMessage = message;
        response.data = null;
        return response;
    }
    
    /**
     * 오류 응답 생성
     */
    public static <T> ApiResponse<T> error(String resultCode, String resultMessage) {
        return new ApiResponse<>(resultCode, resultMessage);
    }
    
    /**
     * 기본 클라이언트 오류 응답
     */
    public static <T> ApiResponse<T> badRequest(String message) {
        return new ApiResponse<>("40003", message != null ? message : "입력값이 유효하지 않습니다.");
    }
    
    /**
     * 인증 오류 응답
     */
    public static <T> ApiResponse<T> unauthorized() {
        return new ApiResponse<>("41001", "로그인이 필요합니다.");
    }
    
    /**
     * 권한 오류 응답
     */
    public static <T> ApiResponse<T> forbidden() {
        return new ApiResponse<>("41003", "접근 권한이 없습니다.");
    }
    
    /**
     * 데이터 없음 오류 응답
     */
    public static <T> ApiResponse<T> notFound(String message) {
        return new ApiResponse<>("43002", message != null ? message : "존재하지 않는 데이터입니다.");
    }
    
    /**
     * 중복 데이터 오류 응답
     */
    public static <T> ApiResponse<T> conflict(String message) {
        return new ApiResponse<>("43001", message != null ? message : "이미 존재하는 데이터입니다.");
    }
    
    /**
     * 서버 오류 응답
     */
    public static <T> ApiResponse<T> internalServerError() {
        return new ApiResponse<>("50001", "서버 내부 오류가 발생했습니다.");
    }
    
    // Getter와 Setter
    public String getGuid() {
        return guid;
    }
    
    public void setGuid(String guid) {
        this.guid = guid;
    }
    
    public String getResultCode() {
        return resultCode;
    }
    
    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }
    
    public String getResultMessage() {
        return resultMessage;
    }
    
    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
    
    // 성공 여부 확인
    public boolean isSuccess() {
        return "00000".equals(this.resultCode);
    }
    
    @Override
    public String toString() {
        return "ApiResponse{" +
                "guid='" + guid + '\'' +
                ", resultCode='" + resultCode + '\'' +
                ", resultMessage='" + resultMessage + '\'' +
                ", data=" + data +
                '}';
    }
}
