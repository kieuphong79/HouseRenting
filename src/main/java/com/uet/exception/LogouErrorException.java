package com.uet.exception;

public class LogouErrorException extends Exception {
    public LogouErrorException() {
        super("Lỗi đăng xuất");
    }
    public LogouErrorException(String s) {
        super(s);
    }
    
}
