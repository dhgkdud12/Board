package spring.board.controller;

import org.springframework.web.bind.annotation.*;
import spring.board.dto.LoginDto;
import spring.board.dto.UserDto;
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
    public String register(@RequestBody UserDto userDto) {
        return userService.register(userDto);
    }

    // 로그인
    @PostMapping ("/login")
    public String logIn(@RequestBody LoginDto loginDto, HttpServletRequest request) {
        return userService.login(loginDto, request);
    }
    
    // 로그아웃
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        return userService.logout(request);
    }
}
