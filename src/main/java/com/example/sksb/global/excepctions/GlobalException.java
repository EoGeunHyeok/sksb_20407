package com.example.sksb.global.excepctions;

public class GlobalException extends  RuntimeException {
    public GlobalException(String resultCode, String msg) {
        super("resultCode=" + resultCode + ",msg=" +msg);
    }
}
