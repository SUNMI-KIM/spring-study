package com.sunmi.httpstatus.Error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public enum ErrorCode {

    USERID_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),
    DUPLICATED_USER_ID(HttpStatus.BAD_REQUEST, "유저아이디가 중복됩니다.");

    private HttpStatus httpStatus;
    private String message;
}
