package com.sunmi.httpstatus.Error;

import lombok.Getter;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Getter
public class MyException extends RuntimeException {

    private final String result = "ERROR";
    private ErrorCode errorCode;
    private String message;

    public MyException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }
}
