package spring.board.controller;

import org.springframework.web.bind.annotation.*;
import spring.board.domain.response.CommonResponse;
import spring.board.domain.response.ResponseStatus;
import spring.board.dto.user.UserLoginRequest;
import spring.board.dto.user.UserRequest;
import spring.board.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 회원가입
    @PostMapping("")
    public CommonResponse register(@RequestBody UserRequest userRequest) {
        String message = userService.register(userRequest);
        return new CommonResponse(ResponseStatus.SUCCESS, message, null);
    }

    // 로그인
    @PostMapping ("/login")
    public CommonResponse logIn(@RequestBody UserLoginRequest userLoginRequest) throws Exception {
        String message = userService.login(userLoginRequest);
        return new CommonResponse(ResponseStatus.SUCCESS, message, null);
    }
    
    // 로그아웃
    @GetMapping("/logout")
    public CommonResponse logout() throws Exception {
        String message = userService.logout();
        return new CommonResponse(ResponseStatus.SUCCESS, message, null);
    }
}
