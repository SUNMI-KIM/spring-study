package jwt.registerLogin.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USERID_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),
    DUPLICATED_USER_ID(HttpStatus.BAD_REQUEST, "유저아이디가 중복됩니다."),

    UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "왜 에러임? 몰?루"),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "권한이 없습니다."),
    WRONG_TOKEN(HttpStatus.UNAUTHORIZED, "잘못된 토큰입니다."),

    NOT_FOUND(HttpStatus.NOT_FOUND, "잘못된 접근입니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 측 기술 오류");

    private HttpStatus httpStatus;
    private String message;
}
