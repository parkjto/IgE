package com.SW.IgE.exception;

import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // UserNotFoundException을 처리하는 메서드
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFoundException(UserNotFoundException ex) {
        logger.warn("UserNotFoundException: {}", ex.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("error", "USER_NOT_FOUND");
        error.put("message", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // InvalidPasswordException을 처리하는 메서드
    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<Map<String, String>> handleInvalidPasswordException(InvalidPasswordException ex) {
        logger.warn("InvalidPasswordException: {}", ex.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("error", "INVALID_PASSWORD");
        error.put("message", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // MenuNotFoundException을 처리하는 메서드
    @ExceptionHandler(MenuNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleMenuNotFoundException(MenuNotFoundException ex) {
        logger.warn("MenuNotFoundException: {}", ex.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("error", "MENU_NOT_FOUND");
        error.put("message", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // InvalidAllergyException을 처리하는 메서드
    @ExceptionHandler(InvalidAllergyException.class)
    public ResponseEntity<Map<String, String>> handleInvalidAllergyException(InvalidAllergyException ex) {
        logger.warn("InvalidAllergyException: {}", ex.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("error", "INVALID_ALLERGY");
        error.put("message", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // IllegalArgumentException을 처리하는 메서드
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        logger.warn("IllegalArgumentException: {}", ex.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("error", "INVALID_INPUT");
        error.put("message", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // IllegalStateException을 처리하는 메서드
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleIllegalStateException(IllegalStateException ex) {
        logger.warn("IllegalStateException: {}", ex.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("error", "ILLEGAL_STATE");
        error.put("message", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // BadRequestException을 처리하는 메서드
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, String>> handleBadRequestException(BadRequestException ex) {
        logger.warn("BadRequestException: {}", ex.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("error", "BAD_REQUEST");
        error.put("message", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // 유효성 검증 실패 처리 (MethodArgumentNotValidException)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
        logger.warn("ValidationException: {}", ex.getBindingResult().getAllErrors());
        Map<String, String> error = new HashMap<>();
        error.put("error", "VALIDATION_FAILED");
        error.put("message", "유효성 검사 실패: " + ex.getBindingResult().getAllErrors().toString());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // BindException 처리 (바인딩 실패 시)
    @ExceptionHandler(BindException.class)
    public ResponseEntity<Map<String, String>> handleBindException(BindException ex) {
        logger.warn("BindException: {}", ex.getAllErrors());
        Map<String, String> error = new HashMap<>();
        error.put("error", "BINDING_FAILED");
        error.put("message", "잘못된 입력: " + ex.getAllErrors().toString());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // 기타 모든 예외를 처리하는 메서드
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        logger.error("Unexpected error occurred", ex);
        Map<String, String> error = new HashMap<>();
        error.put("error", "INTERNAL_SERVER_ERROR");
        error.put("message", "서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
