package spring.board.controller;

import org.springframework.web.bind.annotation.*;
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
    public String register(@RequestBody UserRequest userRequest) {
        return userService.register(userRequest);
    }

    // 로그인
    @PostMapping ("/login")
    public String logIn(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        return userService.login(userLoginRequest, request);
    }
    
    // 로그아웃
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        return userService.logout(request);
    }
}
