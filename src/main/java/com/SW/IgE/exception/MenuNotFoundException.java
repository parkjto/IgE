package com.SW.IgE.exception;

/**
 * 메뉴를 찾을 수 없을 때 발생하는 예외
 */
public class MenuNotFoundException extends RuntimeException {
    
    public MenuNotFoundException(String message) {
        super(message);
    }
    
    public MenuNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}





























