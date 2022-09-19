package spring.board.controller;

import org.springframework.web.bind.annotation.*;
import spring.board.dto.LoginDto;
import spring.board.dto.UserDto;
import spring.board.entity.User;
import spring.board.session.SessionManager;
import spring.board.service.UserService;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final SessionManager sessionManager;

    public UserController(UserService userService, SessionManager sessionManager) {
        this.userService = userService;
        this.sessionManager = sessionManager;
    }

    // 회원가입
    @PostMapping("")
    public String register(@RequestBody UserDto userDto) {
        return userService.register(userDto);
    }

    // 로그인
    @PostMapping ("/login")
    public String logIn(@RequestBody LoginDto loginDto, HttpServletRequest request) {
        User loginUser = userService.login(loginDto, request);
        String result = "";
        if (loginUser == null) {
            result = "로그인 실패";
        } else {
            result = "로그인 성공";
        }
        return result;
    }
    
    // 로그아웃
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        return userService.logout(request);
    }
}
