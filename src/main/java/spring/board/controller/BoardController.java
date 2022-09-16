package spring.board.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.board.entity.User;
import spring.board.session.SessionManager;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("board")
public class BoardController {
    private final SessionManager sessionManager;

    public BoardController(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    // 홈화면
    @GetMapping("")
    public void home(HttpServletRequest request) {
        User user = (User) sessionManager.getSession(request);

        if (user != null) {
            System.out.println(user.getName()+"님 로그인중");
        }
    }


    // 게시물 작성
    // 파일 처리
    //
    
    // 게시물 수정
    
    // 게시물 삭제
}
