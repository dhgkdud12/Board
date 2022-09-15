package spring.board.controller;

import org.springframework.web.bind.annotation.*;
import spring.board.dto.UserDto;
import spring.board.entity.User;
import spring.board.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 회원가입
    // 아이디, 이름, 비밀번호, 이메일 입력받기
    @PostMapping("")
    public String register(@RequestBody UserDto userDto) {
        return userService.register(userDto);
    }

    // 로그인
    // 아이디, 비밀번호 입력 받기
    // 틀렸을 경우, 맞았을 경우
    @GetMapping("")
    public void logIn(@RequestBody User user) {

    }
}
