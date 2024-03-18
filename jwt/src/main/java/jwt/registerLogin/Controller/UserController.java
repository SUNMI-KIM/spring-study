package jwt.registerLogin.Controller;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jwt.registerLogin.Domain.User;
import jwt.registerLogin.Domain.UserRole;
import jwt.registerLogin.Dto.TokenDto;
import jwt.registerLogin.Dto.UserDto;
import jwt.registerLogin.Exception.ErrorCode;
import jwt.registerLogin.Exception.ExceptionDto;
import jwt.registerLogin.Exception.MyException;
import jwt.registerLogin.Repository.UserRepository;
import jwt.registerLogin.Security.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static jwt.registerLogin.Exception.ErrorCode.DUPLICATED_USER_ID;
import static jwt.registerLogin.Exception.ErrorCode.USERID_NOT_FOUND;

@Tag(name = "JWT", description = "JWT 테스트 API")
@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "회원가입", description = "유저 서비스 회원 가입 기능, role 부분은 안 보내도 됌")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "유저 아이디 중복으로 인한 실패", content = @Content(
                    schema = @Schema(implementation = ExceptionDto.class)))
    })
    @PostMapping("/register")
    public UserDto register(@RequestBody User user){
        if (userRepository.find(user.getEmail()) != null) {
            throw new MyException(DUPLICATED_USER_ID);
        }
        user.setRole(UserRole.USER);
        user.hashPassWord(passwordEncoder);
        userRepository.save(user);

        return new UserDto(user);
    }

    @Operation(summary = "로그인", description = "유저 서비스 로그인 기능")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "404", description = "유저 아이디를 찾을 수 없음", content = @Content(
                    schema = @Schema(implementation = ExceptionDto.class)))
    })
    @GetMapping("/login")
    public TokenDto login(@RequestParam("email") String email, @RequestParam("passWord") String password) {
        User user = userRepository.find(email);
        if (!user.checkPassWord(password, passwordEncoder)) {
            throw new MyException(USERID_NOT_FOUND);
        }
        TokenDto token = new TokenDto(jwtProvider.createAccessToken(user.getEmail(), user.getRole()), jwtProvider.createRefreshToken());
        return token;
    }

    @Operation(summary = "유저 조회", description = "유저 개인 조회 기능")
    @Parameter(name = "Authorization", description = "accessToken 담아 전달", in = ParameterIn.HEADER)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "404", description = "유저 아이디를 찾을 수 없음", content = @Content(
                    schema = @Schema(implementation = ExceptionDto.class)))
    })
    @GetMapping("/user/get")
    public User getUser(Authentication authentication) {
        String email = authentication.getName();
        if (userRepository.find(email) == null) {
            throw new MyException(USERID_NOT_FOUND);
        }
        return userRepository.find(email);
    }

    @Operation(summary = "모든 유저 조회", description = "모든 유저 조회 기능")
    @GetMapping("/users")
    public Map<String, User> getUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/refresh")
    @Operation(summary = "토큰 재발급", description = "토큰 만료 시 토큰 재발급 기능")
    @Parameter(name = "RefreshToken", description = "refreshToken 담아 전달", in = ParameterIn.HEADER)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "401", description = "잘못된 토큰입니다.", content = @Content(
                    schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "404", description = "유저 아이디를 찾을 수 없음", content = @Content(
                    schema = @Schema(implementation = ExceptionDto.class)))
    })
    public TokenDto refresh(@RequestParam String accessToken,
                        @RequestHeader("RefreshToken") String refreshToken) {
        String email;
        try {
            jwtProvider.validateToken(refreshToken);
            accessToken = jwtProvider.disassembleToken(accessToken);
            email = jwtProvider.getUser(accessToken);
        } catch (SignatureException | MalformedJwtException | ArrayIndexOutOfBoundsException e) {
            throw new MyException(ErrorCode.WRONG_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new MyException(ErrorCode.EXPIRED_TOKEN);
        }
        if (userRepository.find(email) == null) {
            throw new MyException(USERID_NOT_FOUND);
        }
        User user = userRepository.find(jwtProvider.getUser(accessToken));
        accessToken = jwtProvider.createAccessToken(user.getEmail(), user.getRole());
        refreshToken = jwtProvider.createRefreshToken();
        return new TokenDto(accessToken, refreshToken);
    }
}
