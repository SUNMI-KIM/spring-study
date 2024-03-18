package com.sunmi.httpstatus.Error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class ExceptionDto {
    private ErrorCode errorCode;
    private String message;
}
