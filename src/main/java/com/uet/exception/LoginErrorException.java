package com.uet.exception;

public class LoginErrorException extends Exception{
    public LoginErrorException() {
        super("Lỗi đăng nhập");
    }
    public LoginErrorException(String s) {
        super(s);
    }
}
