package spring.board.controller;

import org.springframework.web.bind.annotation.*;
import spring.board.common.response.CommonResponse;
import spring.board.common.response.ResponseStatus;
import spring.board.dto.user.UserLoginRequest;
import spring.board.dto.user.UserRequest;
import spring.board.service.UserService;

import javax.validation.Valid;
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 회원가입
    @PostMapping("")
    public CommonResponse register(@Valid @RequestBody UserRequest userRequest) {
        return userService.register(userRequest);
    }

    // 로그인
    @PostMapping ("/login")
    public CommonResponse logIn(@RequestBody UserLoginRequest userLoginRequest) {
        return userService.login(userLoginRequest);
    }
    
    // 로그아웃
    @GetMapping("/logout")
    public CommonResponse logout() {
        String message = userService.logout();
        return new CommonResponse(ResponseStatus.SUCCESS, 200, message, null);
    }
}
