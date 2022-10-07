package spring.board.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import spring.board.dto.BoardResponse;
import spring.board.dto.CommentResponse;
import spring.board.dto.UserSession;
import spring.board.service.BoardService;
import spring.board.service.CommentService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Map myBoards(
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "blockSize",  required = false, defaultValue = "10") int blockSize,
            HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("boardInfo", boardService.selectAllPosts(page, size, blockSize));
        resultMap.put("pageInfo", boardService.getPagingInfo(page, size, blockSize));
        return resultMap;
    }
    
    // 내가 작성한 댓글
    @GetMapping("/comments")
    public List<CommentResponse> myComments(HttpServletRequest request) {
        return commentService.selectCommentsByUserId(request);
    }

}
