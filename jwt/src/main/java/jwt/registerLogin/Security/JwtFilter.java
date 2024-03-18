package jwt.registerLogin.Security;

import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jwt.registerLogin.Exception.ErrorCode;
import jwt.registerLogin.Exception.MyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Bearer 검증
        String token = jwtProvider.resolveToken(request);
        try {
            if (token != null && jwtProvider.validateToken(token)) {
                token = jwtProvider.disassembleToken(token);
                Authentication auth = jwtProvider.getUserRole(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (SignatureException | MalformedJwtException e) {
            request.setAttribute("exception", ErrorCode.WRONG_TOKEN.getMessage());
        } catch (ExpiredJwtException e) {
            request.setAttribute("exception", ErrorCode.EXPIRED_TOKEN.getMessage());
        } catch (Exception e) {
            log.error("================================================");
            log.error("JwtFilter - doFilterInternal() 오류발생");
            log.error("Exception Message : {}", e.getMessage());
            log.error("Exception StackTrace : {");
            e.printStackTrace();
            log.error("}");
            log.error("================================================");
            request.setAttribute("exception", ErrorCode.UNKNOWN_ERROR.getMessage());
        }
        filterChain.doFilter(request, response);
    }
}
