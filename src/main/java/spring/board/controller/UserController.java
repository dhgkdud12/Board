package spring.board.controller;

import org.springframework.web.bind.annotation.*;
import spring.board.dto.LoginDto;
import spring.board.dto.UserDto;
import spring.board.entity.User;
import spring.board.session.SessionManager;
import spring.board.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


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
    // 아이디, 비밀번호 입력 받기
    // 틀렸을 경우, 맞았을 경우
    @PostMapping ("/login")
    public String logIn(@RequestBody LoginDto loginDto, HttpServletResponse response) {
        String result = "";
        User loginUser = userService.login(loginDto);
        if (loginUser == null) {
            result = "로그인 실패";
        } else {
            result = "로그인 성공";
            sessionManager.createSession(loginUser, response);
        }
        return result;
    }
    
    // 로그아웃
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        sessionManager.expire(request);
        return "로그아웃 완료";
    }
}
