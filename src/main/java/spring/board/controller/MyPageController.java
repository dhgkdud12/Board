package spring.board.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.board.dto.BoardResponse;
import spring.board.dto.CommentResponse;
import spring.board.service.BoardService;
import spring.board.service.CommentService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/myPage")
public class MyPageController {

    private final BoardService boardService;
    private final CommentService commentService;


    public MyPageController(BoardService boardService, CommentService commentService) {
        this.boardService = boardService;
        this.commentService = commentService;
    }

    // 내가 작성한 글
    @GetMapping("/boards")
    public List<BoardResponse> myBoards(HttpServletRequest request) {
        return boardService.selectPostByUserId(request);
    }
    
    // 내가 작성한 댓글
    @GetMapping("/comments")
    public List<CommentResponse> myComments(HttpServletRequest request) {
        return commentService.selectCommentsByUserId(request);
    }

}
