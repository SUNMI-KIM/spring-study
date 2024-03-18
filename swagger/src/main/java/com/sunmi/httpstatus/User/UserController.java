package com.sunmi.httpstatus.User;

import com.sunmi.httpstatus.Error.ExceptionDto;
import com.sunmi.httpstatus.Error.MyException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.sunmi.httpstatus.Error.ErrorCode.USERID_NOT_FOUND;

@Tag(name = "http status", description = "상태코드 테스트 API")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @Operation(summary = "회원가입", description = "유저 서비스 회원 가입 기능")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "유저 아이디 중복으로 인한 실패", content = @Content(
                    schema = @Schema(implementation = ExceptionDto.class)))
    })
    @PostMapping("/user")
    public User register(@RequestBody User user) {
        userRepository.save(user);
        return user;
    }

    @Operation(summary = "유저 조회", description = "회원가입한 유저를 조회하는 기능")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "404", description = "유저 아이디를 찾을 수 없음", content = @Content(
                    schema = @Schema(implementation = ExceptionDto.class)))
    })
    @GetMapping("/user")
    public User getUser(@RequestParam int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new MyException(USERID_NOT_FOUND));
        return user;
    }
}
