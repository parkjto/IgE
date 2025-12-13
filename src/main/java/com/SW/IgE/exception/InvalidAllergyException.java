package com.SW.IgE.exception;

/**
 * 유효하지 않은 알레르기 유형일 때 발생하는 예외
 */
public class InvalidAllergyException extends RuntimeException {
    
    public InvalidAllergyException(String message) {
        super(message);
    }
    
    public InvalidAllergyException(String message, Throwable cause) {
        super(message, cause);
    }
}





























