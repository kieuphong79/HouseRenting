package com.uet.exception;

public class LogoutErrorException extends Exception {
    public LogoutErrorException() {
        super("Lỗi đăng xuất");
    }
    public LogoutErrorException(String s) {
        super(s);
    }
    
}
