package com.SW.IgE.exception;

/**
 * 비밀번호 유효성 검사 실패 시 발생하는 예외
 */
public class InvalidPasswordException extends RuntimeException {
    
    public InvalidPasswordException(String message) {
        super(message);
    }
    
    public InvalidPasswordException(String message, Throwable cause) {
        super(message, cause);
    }
}





























