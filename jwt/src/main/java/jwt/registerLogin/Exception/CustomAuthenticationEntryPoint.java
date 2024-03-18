package jwt.registerLogin.Exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String exception = (String) request.getAttribute("exception");
        response.setContentType("application/json;charset=UTF-8");

        ExceptionDto exceptionDto = new ExceptionDto();

        if (exception == null) {
            exceptionDto.setErrorCode(ErrorCode.NOT_FOUND);
            exceptionDto.setMessage(ErrorCode.NOT_FOUND.getMessage());
        } else if (exception.equals(ErrorCode.UNKNOWN_ERROR.getMessage())) {
            exceptionDto.setErrorCode(ErrorCode.UNKNOWN_ERROR);
            exceptionDto.setMessage(ErrorCode.UNKNOWN_ERROR.getMessage());
        } else if (exception.equals(ErrorCode.WRONG_TOKEN.getMessage())) {
            exceptionDto.setErrorCode(ErrorCode.WRONG_TOKEN);
            exceptionDto.setMessage(ErrorCode.WRONG_TOKEN.getMessage());
        } else if (exception.equals(ErrorCode.EXPIRED_TOKEN.getMessage())) {
            exceptionDto.setErrorCode(ErrorCode.EXPIRED_TOKEN);
            exceptionDto.setMessage(ErrorCode.EXPIRED_TOKEN.getMessage());
        }
        setResponse(response, exceptionDto);
    }

    private void setResponse(HttpServletResponse response, ExceptionDto exceptionDto) throws IOException {
        String result = objectMapper.writeValueAsString(exceptionDto);
        response.setStatus(exceptionDto.getErrorCode().getHttpStatus().value());
        response.getWriter().write(result);
    }
}
