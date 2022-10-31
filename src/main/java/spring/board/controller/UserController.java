package spring.board.controller;

import org.springframework.web.bind.annotation.*;
import spring.board.domain.response.CommonResponse;
import spring.board.domain.response.ResponseStatus;
import spring.board.dto.user.UserLoginRequest;
import spring.board.dto.user.UserRequest;
import spring.board.service.UserService;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 회원가입
    @PostMapping("")
    public CommonResponse register(@RequestBody UserRequest userRequest) throws Exception {
        String message = userService.register(userRequest);
        return new CommonResponse(ResponseStatus.SUCCESS, 200, message, null);
    }

    // 로그인
    @PostMapping ("/login")
    public CommonResponse logIn(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        String message = userService.login(userLoginRequest, request);
        return new CommonResponse(ResponseStatus.SUCCESS, 200, message, null);
    }
    
    // 로그아웃
    @GetMapping("/logout")
    public CommonResponse logout(HttpServletRequest request) {
        String message = userService.logout(request);
        return new CommonResponse(ResponseStatus.SUCCESS, 200, message, null);
    }
}
