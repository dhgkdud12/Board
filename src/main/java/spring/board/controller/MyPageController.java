package spring.board.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import spring.board.domain.response.CommonResponse;
import spring.board.domain.response.ResponseStatus;
import spring.board.dto.comment.CommentResponse;
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
    public CommonResponse myBoards(
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "blockSize",  required = false, defaultValue = "10") int blockSize,
            HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("boardInfo", boardService.selectPostsByUserId(page, size, blockSize, request));
        resultMap.put("pageInfo", boardService.getPagingInfo(page, size, blockSize));
        return new CommonResponse<>(ResponseStatus.SUCCESS, 200, "게시물 조회 완료", resultMap);
    }
    
    // 내가 작성한 댓글
    @GetMapping("/comments")
    public CommonResponse myComments(HttpServletRequest request) {
        List<CommentResponse> comments = commentService.selectCommentsByUserId(request);
        return new CommonResponse<>(ResponseStatus.SUCCESS, 200, "댓글 조회 완료", comments);
    }

}
