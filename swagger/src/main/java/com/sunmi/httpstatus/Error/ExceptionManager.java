package com.sunmi.httpstatus.Error;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionManager {

    @ExceptionHandler(MyException.class)
    public ResponseEntity<ExceptionDto> myExceptionHandler(MyException e) {
        e.printStackTrace();
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(new ExceptionDto(e.getErrorCode(), e.getMessage()));
    }

}
